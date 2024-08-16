package com.example.we_save.domain.notification.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.notification.controller.request.NotificationRequestDto;
import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
import com.example.we_save.domain.user.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {

    @Transactional(readOnly = true)
    List<NotificationResponseDto> getNotifications(User user, int page, int size, boolean excludeCompleted);

    // 특정 사용자의 모든 알림 조회
    ApiResponse<List<NotificationResponseDto>> getNotifications(User user, int page, boolean excludeCompleted);

    // 댓글 알림 생성
    ApiResponse<Void> createCommentNotification(User user, NotificationRequestDto requestDto);

    // 버튼 알림 생성 (확인했어요, 허위예요)
    ApiResponse<Void> createButtonNotification(User user, NotificationRequestDto requestDto);

    // 인기 게시글 알림 생성 (확인했어요 버튼 10개 이상)
    ApiResponse<Void> createPopularNotification(User user, NotificationRequestDto requestDto);

    // 상황 종료 알림 생성
    ApiResponse<Void> createStatusNotification(User user, NotificationRequestDto requestDto);

    // 알림 읽음 상태로 변경
    ApiResponse<Void> markNotificationAsRead(User user, Long notificationId);

    SseEmitter streamNotifications(User user);
}
