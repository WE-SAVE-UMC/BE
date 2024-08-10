package com.example.we_save.domain.notification.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
import com.example.we_save.domain.notification.entity.Notification;
import com.example.we_save.domain.notification.entity.NotificationButton;
import com.example.we_save.domain.notification.repository.NotificationButtonRepository;
import com.example.we_save.domain.notification.repository.NotificationRepository;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.repository.PostRepository;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationButtonRepository notificationButtonRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ApiResponse<NotificationResponseDto> createNotification(Long postId, Long userId) {
        return null;
    }

    @Override
    public ApiResponse<Void> markAsRead(Long notificationId) {
        return null;
    }

    @Override
    public ApiResponse<List<NotificationResponseDto>> getNotificationsByUser(Long userId) {
        return null;
    }

    @Override
    public void checkAndNotify(Long postId) {

    }

    @Override
    public ApiResponse<List<NotificationResponseDto>> getNotificationsByConfirmCount(int confirmCount) {
        List<Notification> notifications = notificationRepository.findAll().stream()
                .filter(notification -> notification.getConfirmCount() >= confirmCount)
                .toList();

        List<NotificationResponseDto> responseDtos = notifications.stream()
                .map(notification -> new NotificationResponseDto(
                        notification.getId(),
                        notification.getTitle(),
                        notification.getCreatedAt(),
                        notification.isRead()
                ))
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(responseDtos);
    }

    @Override
    public ApiResponse<NotificationResponseDto> createCommentNotification(Long postId, Long userId, Long commentId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Notification notification = new Notification();
        notification.setPost(post);
        notification.setUser(user);
        notification.setCommentId(commentId);
        notification.setContent(content);
        notification.setRead(false);

        notificationRepository.save(notification);

        NotificationResponseDto responseDto = new NotificationResponseDto(
                notification.getId(),
                notification.getContent(),
                notification.getCreatedAt(),
                notification.isRead()
        );

        return ApiResponse.onGetSuccess(responseDto);
    }

    @Override
    public ApiResponse<NotificationResponseDto> createButtonNotification(Long postId, Long userId, String buttonType) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String oppositeButtonType = buttonType.equals("확인했어요") ? "허위예요" : "확인했어요";
        if (notificationButtonRepository.existsByPostIdAndButtonType(postId, oppositeButtonType)) {
            return ApiResponse.onFailure("Cannot press both buttons for the same post", "CONFLICT", null);
        }

        NotificationButton notificationButton = new NotificationButton();
        notificationButton.setPost(post);
        notificationButton.setUser(user);
        notificationButton.setButtonType(buttonType);
        notificationButton.setRead(false);

        notificationButtonRepository.save(notificationButton);

        NotificationResponseDto responseDto = new NotificationResponseDto(
                notificationButton.getNotificationId(),
                userId,
                postId,
                buttonType,
                notificationButton.getCreatedAt(),
                notificationButton.isRead()
        );

        return ApiResponse.onGetSuccess(responseDto);
    }

    @Override
    public ApiResponse<List<NotificationResponseDto>> getButtonNotificationsByPost(Long postId, String buttonType) {
        return null;
    }

}