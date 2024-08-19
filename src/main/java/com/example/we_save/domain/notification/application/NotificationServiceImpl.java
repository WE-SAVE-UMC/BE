package com.example.we_save.domain.notification.application;

//import com.example.we_save.apiPayload.ApiResponse;
//import com.example.we_save.apiPayload.code.status.SuccessStatus;
//import com.example.we_save.domain.notification.controller.request.NotificationRequestDto;
//import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
//import com.example.we_save.domain.notification.entity.Notification;
//import com.example.we_save.domain.notification.entity.NotificationButton;
//import com.example.we_save.domain.notification.entity.NotificationComment;
//import com.example.we_save.domain.notification.repository.NotificationButtonRepository;
//import com.example.we_save.domain.notification.repository.NotificationCommentRepository;
//import com.example.we_save.domain.notification.repository.NotificationRepository;
//import com.example.we_save.domain.post.entity.Post;
//import com.example.we_save.domain.post.repository.PostRepository;
//import com.example.we_save.domain.user.entity.User;
//import com.example.we_save.domain.user.repository.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentMap;
//import java.util.stream.Collectors;
//import java.io.IOException;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationServiceImpl implements NotificationService {
//
//    @Autowired
//    private final NotificationRepository notificationRepository;
//
//    @Autowired
//    private final PostRepository postRepository;
//
//    @Autowired
//    private TaskScheduler taskScheduler;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private final NotificationCommentRepository notificationCommentRepository;
//
//    @Autowired
//    private final NotificationButtonRepository notificationButtonRepository;
//
//    private final int PAGE_SIZE = 10;
//
//    private final ConcurrentMap<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
//
//    @Override
//    @Transactional
//    public List<NotificationResponseDto> getNotifications(User user, int page, int size, boolean excludeCompleted) {
//        Pageable pageable = PageRequest.of(page, size);
//
//        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
//
//        return notifications.stream()
//                .map(NotificationResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public ApiResponse<List<NotificationResponseDto>> getNotifications(User user, int page, boolean excludeCompleted) {
//        return null;
//    }
//
////    @Override
////    @Transactional
////    public ApiResponse<Void> createCommentNotification(User user, NotificationRequestDto requestDto) {
////        Post post = postRepository.findById(requestDto.getPostId())
////                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));
////
////        List<User> allUsers = userRepository.findAll();
////
////        for (User recipient : allUsers) {
////            Notification notification = Notification.builder()
////                    .user(recipient)
////                    .post(post)
////                    .title(requestDto.getTitle())
////                    .confirmCount(requestDto.getConfirmCount())
////                    .isRead(false)
////                    .createdAt(LocalDateTime.now())
////                    .build();
////
////            notificationRepository.save(notification);
////            sendNotificationToUser(recipient, notification); // SSE로 실시간 알림 전송
////
////        }
////        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
////    }
//
//    @Override
//    @Transactional
//    public ApiResponse<Void> createCommentNotification(User user, NotificationRequestDto requestDto) {
//        Post post = postRepository.findById(requestDto.getPostId())
//                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));
//
//        NotificationComment notificationComment = NotificationComment.builder()
//                .user(user)
//                .post(post)
//                .commenterName(requestDto.getAuthorName())
//                .content(requestDto.getContent())
//                .isRead(false)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        notificationCommentRepository.save(notificationComment); // NotificationComment 저장
//
//        User postAuthor = post.getUser();
//        sendCommentNotificationToUser(postAuthor, notificationComment);
//
//        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
//    }
//
//    public void sendCommentNotificationToUser(User user, NotificationComment notificationComment) {
//        SseEmitter emitter = sseEmitters.get(user.getId());
//
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("commentNotification")
//                        .data(Map.of(
//                                "postId", notificationComment.getPost().getId(),
//                                "commenterName", notificationComment.getCommenterName(),
//                                "content", notificationComment.getContent(),
//                                "createdAt", notificationComment.getCreatedAt(),
//                                "isRead", notificationComment.isRead()
//                        )));
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//                sseEmitters.remove(user.getId());
//            }
//        }
//    }
//
//
////    @Override
////    @Transactional
////    public ApiResponse<Void> createButtonNotification(User user, NotificationRequestDto requestDto) {
////        Post post = postRepository.findById(requestDto.getPostId())
////                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));
////
////        Notification notification = Notification.builder()
////                .user(user)
////                .post(post)
////                .buttonType(requestDto.getButtonType())
////                .isRead(false)
////                .createdAt(LocalDateTime.now())
////                .build();
////
////        notificationRepository.save(notification);
////
////        User postAuthor = post.getUser();
////        sendNotificationToUser(postAuthor, notification);
////
////        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
////    }
//
//    @Override
//    @Transactional
//    public ApiResponse<Void> createButtonNotification(User user, NotificationRequestDto requestDto) {
//        Post post = postRepository.findById(requestDto.getPostId())
//                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));
//
//        NotificationButton notificationButton = NotificationButton.builder()
//                .user(user)
//                .post(post)
//                .buttonType(requestDto.getButtonType())
//                .isRead(false)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        notificationButtonRepository.save(notificationButton);
//
//        User postAuthor = post.getUser();
//        sendButtonNotificationToUser(postAuthor, notificationButton);
//
//        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
//    }
//
//    public void sendButtonNotificationToUser(User user, NotificationButton notificationButton) {
//        SseEmitter emitter = sseEmitters.get(user.getId());
//
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("buttonNotification")
//                        .data(Map.of(
//                                "postId", notificationButton.getPost().getId(),
//                                "buttonType", notificationButton.getButtonType(),
//                                "createdAt", notificationButton.getCreatedAt(),
//                                "isRead", notificationButton.isRead()
//                        )));
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//                sseEmitters.remove(user.getId());
//            }
//        }
//    }
//
//    @Override
//    @Transactional
//    public ApiResponse<Void> createPopularNotification(User user, NotificationRequestDto requestDto) {
//        Post post = postRepository.findById(requestDto.getPostId())
//                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));
//
//        Notification notification = Notification.builder()
//                .user(user)
//                .post(post)
//                .title(requestDto.getTitle())
//                .confirmCount(requestDto.getConfirmCount())
//                .isRead(false)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        notificationRepository.save(notification);
//        sendNotificationToUser(user, notification);
//
//        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
//    }
//
//    @Override
//    @Transactional
//    public ApiResponse<Void> createStatusNotification(User user, NotificationRequestDto requestDto) {
//        Post post = postRepository.findById(requestDto.getPostId())
//                .orElseThrow(() -> new EntityNotFoundException("Invalid post ID"));
//
//        Notification notification = Notification.builder()
//                .user(user)
//                .post(post)
//                .title(requestDto.getTitle())
//                .expiryTime(LocalDateTime.now().plusHours(1))
//                .isCompleted(false)
//                .isRead(false)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        scheduleNotification(notification);
//        sendNotificationToUser(user, notification);
//
//        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
//    }
//
//    private void scheduleNotification(Notification notification) {
//        Runnable task = () -> {
//            if (!notification.getPost().isCompleted()) {
//                sendNotificationToUser(notification.getUser(), notification);
//            }
//        };
//
//        Instant sendTime = notification.getExpiryTime().atZone(ZoneId.systemDefault()).toInstant();
//        taskScheduler.schedule(task, sendTime);
//    }
//
//    @Override
//    @Transactional
//    public ApiResponse<Void> markNotificationAsRead(User user, Long notificationId) {
//        Notification notification = notificationRepository.findByIdAndUser(notificationId, user)
//                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
//
//        notification.setRead(true);
//        notificationRepository.save(notification);
//
//        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
//    }
//
//    @Override
//    public SseEmitter streamNotifications(User user) {
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // SSE 연결을 위한 Emitter 생성
//
//        // SSE 연결이 끊길 경우에 대한 처리를 등록
//        emitter.onCompletion(() -> sseEmitters.remove(user.getId()));
//        emitter.onTimeout(() -> sseEmitters.remove(user.getId()));
//
//        // Emitter를 저장소에 저장
//        sseEmitters.put(user.getId(), emitter);
//
//        // 클라이언트가 연결할 때 초기 응답 전송 (연결 성공 알림)
//        try {
//            emitter.send(SseEmitter.event().name("init").data("SSE connection established."));
//        } catch (IOException e) {
//            emitter.completeWithError(e);
//        }
//
//        return emitter;
//    }
//
//    // 특정 사용자에게 알림을 전송하는 메서드
//    public void sendNotificationToUser(User user, Notification notification) {
//        SseEmitter emitter = sseEmitters.get(user.getId()); // 저장소에서 해당 사용자에 대한 Emitter 가져오기
//
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("notification")
//                        .data(new NotificationResponseDto(notification)));
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//                sseEmitters.remove(user.getId());
//            }
//        }
//    }
//}
