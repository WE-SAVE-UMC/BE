package com.example.we_save.domain.notification.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
import com.example.we_save.domain.notification.controller.request.NotificationRequestDto;
import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface NotificationService {
    ApiResponse<NotificationResponseDto> createNotification(Long postId, Long userId);
    // 알림 생성

    ApiResponse<Void> markAsRead(Long notificationId);
    // 알림 읽은 상태로 변경

    ApiResponse<List<NotificationResponseDto>> getNotificationsByUser(Long userId);
    // 지금까지 받은 알림

    @Transactional
    void checkAndNotify(Long postId);

    ApiResponse<List<NotificationResponseDto>> getNotificationsByConfirmCount(int confirmCount);
    // 특정 수 이상의 "확인했어요" 받은 알림 조회

    ApiResponse<NotificationResponseDto> createCommentNotification(Long postId, Long userId, Long commentId, String content);
    // 댓글 알림 생성
}
