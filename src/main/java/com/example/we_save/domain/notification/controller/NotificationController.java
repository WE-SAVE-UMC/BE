package com.example.we_save.domain.notification.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.notification.application.NotificationService;
import com.example.we_save.domain.notification.controller.request.NotificationRequestDto;
import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDto>> createNotification(
            @RequestBody NotificationRequestDto notificationRequestDto) {
        ApiResponse<NotificationResponseDto> responseDto = notificationService.createNotification(
                notificationRequestDto.getPostId(),
                notificationRequestDto.getUserId());
        return ResponseEntity.ok(responseDto);
    }
    // 알림 생성

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getUserNotifications(
//            @PathVariable("userId") Long userId) {
//        ApiResponse<List<NotificationResponseDto>> response = notificationService.getNotificationsByUser(userId);
//        return ResponseEntity.ok(response);
//    }
//    // 지금까지 받은 모든 알림

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getNotificationsByConfirmCount(
            @RequestParam("confirmCount") int confirmCount) {
        ApiResponse<List<NotificationResponseDto>> response = notificationService.getNotificationsByConfirmCount(confirmCount);
        return ResponseEntity.ok(response);
    }
    // 확인했어요 버튼 특정 수 이상(신뢰도 높은 사고)

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> createCommentNotification(
            @PathVariable("commentId") Long commentId,
            @RequestBody NotificationRequestDto notificationRequestDto) {
        ApiResponse<NotificationResponseDto> responseDto = notificationService.createCommentNotification(
                notificationRequestDto.getPostId(),
                notificationRequestDto.getUserId(),
                commentId,
                notificationRequestDto.getContent());
        return ResponseEntity.ok(responseDto);
    }
    // 댓글 알림 생성

    @PostMapping("/buttons")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> createButtonNotification(
            @RequestBody NotificationRequestDto notificationRequestDto) {
        ApiResponse<NotificationResponseDto> responseDto = notificationService.createButtonNotification(
                notificationRequestDto.getPostId(),
                notificationRequestDto.getUserId(),
                notificationRequestDto.getButtonType());
        return ResponseEntity.ok(responseDto);
    }
    // 버튼 알림 생성 (확인했어요 또는 허위예요)

//    @PostMapping("/{notificationId}/confirm")
//    public ResponseEntity<ApiResponse<Void>> confirmNotification(
//            @PathVariable("notificationId") Long notificationId,
//            @RequestParam("userId") Long userId) {
//        ApiResponse<Void> response = notificationService.confirmNotification(notificationId, userId);
//        return ResponseEntity.ok(response);
//    }
//    // 확인했어요 버튼

//    @PostMapping("/{notificationId}/report-false")
//    public ResponseEntity<ApiResponse<Void>> reportFalseNotification(
//            @PathVariable("notificationId") Long notificationId,
//            @RequestParam("userId") Long userId) {
//        ApiResponse<Void> response = notificationService.reportFalseNotification(notificationId, userId);
//        return ResponseEntity.ok(response);
//    }
//    // 허위예요 버튼
}
