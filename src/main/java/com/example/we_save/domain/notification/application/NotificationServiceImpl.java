package com.example.we_save.domain.notification.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
import com.example.we_save.domain.notification.entity.Notification;
import com.example.we_save.domain.notification.repository.NotificationRepository;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.repository.PostRepository;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

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
                .collect(Collectors.toList());

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
}