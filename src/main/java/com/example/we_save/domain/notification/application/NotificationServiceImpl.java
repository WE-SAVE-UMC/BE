package com.example.we_save.domain.notification.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.notification.controller.request.NotificationRequestDto;
import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
import com.example.we_save.domain.notification.entity.Notification;
import com.example.we_save.domain.notification.repository.NotificationRepository;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.repository.PostRepository;
import com.example.we_save.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private final PostRepository postRepository;

    private final int PAGE_SIZE = 10;

    private final ConcurrentMap<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public List<NotificationResponseDto> getNotifications(User user, int page, int size, boolean excludeCompleted) {
        Pageable pageable = PageRequest.of(page, size);
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);

        if (excludeCompleted) {
            notifications = notifications.stream()
                    .filter(notification -> !notification.isCompleted() && notification.getExpiryTime().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }

        return notifications.stream()
                .map(NotificationResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<List<NotificationResponseDto>> getNotifications(User user, int page, boolean excludeCompleted) {
        return null;
    }

    @Override
    @Transactional
    public ApiResponse<Void> createCommentNotification(User user, NotificationRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));

        Notification notification = Notification.builder()
                .user(user)
                .post(post)
                .commentId(requestDto.getCommentId())
                .content(requestDto.getContent())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<Void> createButtonNotification(User user, NotificationRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));

        Notification notification = Notification.builder()
                .user(user)
                .post(post)
                .buttonType(requestDto.getButtonType())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<Void> createPopularNotification(User user, NotificationRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));

        Notification notification = Notification.builder()
                .user(user)
                .post(post)
                .title(requestDto.getTitle())
                .confirmCount(requestDto.getConfirmCount())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<Void> createStatusNotification(User user, NotificationRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));

        Notification notification = Notification.builder()
                .user(user)
                .post(post)
                .title(requestDto.getTitle())
                .expiryTime(requestDto.getExpiryTime())
                .isCompleted(false)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<Void> markNotificationAsRead(User user, Long notificationId) {
        Notification notification = notificationRepository.findByIdAndUser(notificationId, user)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    public SseEmitter streamNotifications(User user) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // SSE 연결을 위한 Emitter 생성

        // SSE 연결이 끊길 경우에 대한 처리를 등록
        emitter.onCompletion(() -> sseEmitters.remove(user.getId()));
        emitter.onTimeout(() -> sseEmitters.remove(user.getId()));

        // Emitter를 저장소에 저장
        sseEmitters.put(user.getId(), emitter);

        // 클라이언트가 연결할 때 초기 응답 전송 (연결 성공 알림)
        try {
            emitter.send(SseEmitter.event().name("init").data("SSE connection established."));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    // 특정 사용자에게 알림을 전송하는 메서드
    public void sendNotificationToUser(User user, Notification notification) {
        SseEmitter emitter = sseEmitters.get(user.getId()); // 저장소에서 해당 사용자에 대한 Emitter 가져오기

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(new NotificationResponseDto(notification)));
            } catch (IOException e) {
                emitter.completeWithError(e);
                sseEmitters.remove(user.getId());
            }
        }
    }
}
