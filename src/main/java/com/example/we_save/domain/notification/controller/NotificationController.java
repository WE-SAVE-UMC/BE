package com.example.we_save.domain.notification.controller;

import com.example.we_save.domain.notification.entity.Notification;
import com.example.we_save.domain.notification.application.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications/stream")
    public SseEmitter streamNotifications() {
        SseEmitter emitter = new SseEmitter();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                Long postId = 1L;
                String postTitle = "사거리 교통사고 발생";
                String notificationType = "COMMENT"; // COMMENT, BUTTON, NEAR_BOARD, TIME_ELAPSED
                String username = "익명1";
                String buttonType = null;
                Integer buttonTotalCount = null;

                Notification notification = notificationService.saveNotification(postId, postTitle, notificationType, username, buttonType, buttonTotalCount);
                emitter.send(notification, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 5, TimeUnit.SECONDS);

        return emitter;
    }

    @PostMapping("/notifications")
    public Notification createNotification(
            @RequestParam Long postId,
            @RequestParam String postTitle,
            @RequestParam String notificationType,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String buttonType,
            @RequestParam(required = false) Integer buttonTotalCount) {

        return notificationService.saveNotification(postId, postTitle, notificationType, username, buttonType, buttonTotalCount);
    }

    @GetMapping("/notifications")
    public List<Notification> getNotifications() {
        return notificationService.getAllNotifications();
    }
}



//import com.example.we_save.domain.notification.entity.Notification;
//import com.example.we_save.domain.notification.application.NotificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//public class NotificationController {
//
//    private final NotificationService notificationService;
//
//    @Autowired
//    public NotificationController(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//
//    @GetMapping("/notifications/stream")
//    public SseEmitter streamNotifications() {
//        SseEmitter emitter = new SseEmitter();
//
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//            try {
//                Long postId = 1L;
//                String postTitle = "사거리 교통사고 발생";
//                String notificationType = "COMMENT"; // COMMENT, BUTTON, NEAR_BOARD, TIME_ELAPSED
//                String username = "익명1";
//                String buttonType = null;
//                Integer buttonTotalCount = null;
//
//                Notification notification = notificationService.saveNotification(postId, postTitle, notificationType, username, buttonType, buttonTotalCount);
//                emitter.send(notification, MediaType.APPLICATION_JSON);
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        }, 0, 5, TimeUnit.SECONDS);
//
//        return emitter;
//    }
//
//    @PostMapping("/notifications")
//    public Notification createNotification(
//            @RequestParam Long postId,
//            @RequestParam String postTitle,
//            @RequestParam String notificationType,
//            @RequestParam(required = false) String username,
//            @RequestParam(required = false) String buttonType,
//            @RequestParam(required = false) Integer buttonTotalCount) {
//
//        return notificationService.saveNotification(postId, postTitle, notificationType, username, buttonType, buttonTotalCount);
//    }
//
//    @GetMapping("/notifications")
//    public List<Notification> getNotifications() {
//        return notificationService.getAllNotifications();
//    }
//}

//import com.example.we_save.apiPayload.ApiResponse;
//import com.example.we_save.apiPayload.code.status.SuccessStatus;
//import com.example.we_save.domain.notification.application.NotificationService;
//import com.example.we_save.domain.notification.controller.request.NotificationRequestDto;
//import com.example.we_save.domain.notification.controller.response.NotificationResponseDto;
//import com.example.we_save.domain.user.entity.User;
//import com.example.we_save.domain.user.service.UserAuthCommandService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.List;
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/notifications")
//@RequiredArgsConstructor
//public class NotificationController {
//
//    @Autowired
//    private final NotificationService notificationService;
//
//    @Autowired
//    private final UserAuthCommandService userAuthCommandService;
//
//    @Operation(summary = "댓글 알림 생성", security = @SecurityRequirement(name = "Authorization"))
//    @PostMapping("/comments")
//    public ResponseEntity<ApiResponse<Void>> createCommentNotification(
//            @RequestBody NotificationRequestDto requestDto) {
//
//        User user = userAuthCommandService.getAuthenticatedUserInfo();
//
//        notificationService.createCommentNotification(user, requestDto);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK));
//    }
//
//    @Operation(summary = "버튼 알림 생성 (확인했어요, 허위예요)", security = @SecurityRequirement(name = "Authorization"))
//    @PostMapping("/buttons")
//    public ResponseEntity<ApiResponse<Void>> createButtonNotification(
//            @RequestBody NotificationRequestDto requestDto) {
//
//        User user = userAuthCommandService.getAuthenticatedUserInfo();
//
//        notificationService.createButtonNotification(user, requestDto);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK));
//    }
//
//    @Operation(summary = "확인했어요 버튼 10개 이상 알림 생성", security = @SecurityRequirement(name = "Authorization"))
//    @PostMapping("/popular")
//    public ResponseEntity<ApiResponse<Void>> createPopularNotification(
//            @RequestBody NotificationRequestDto requestDto) {
//
//        User user = userAuthCommandService.getAuthenticatedUserInfo();
//
//        notificationService.createPopularNotification(user, requestDto);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK));
//    }
//
//    @Operation(summary = "상황 종료 알림 생성", security = @SecurityRequirement(name = "Authorization"))
//    @PostMapping("/status")
//    public ResponseEntity<ApiResponse<Void>> createStatusNotification(
//            @RequestBody NotificationRequestDto requestDto) {
//
//        User user = userAuthCommandService.getAuthenticatedUserInfo();
//
//        notificationService.createStatusNotification(user, requestDto);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK));
//    }
//
//    @Operation(summary = "모든 알림 조회", security = @SecurityRequirement(name = "Authorization"))
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getAllNotifications(
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam(defaultValue = "false") boolean excludeCompleted) {
//
//        User user = userAuthCommandService.getAuthenticatedUserInfo();
//
//        List<NotificationResponseDto> notifications = notificationService.getNotifications(user, page, size, excludeCompleted);
//
//        return ResponseEntity.ok(ApiResponse.onGetSuccess(notifications));
//    }
//
//    @Operation(summary = "알림 읽음 상태 변경", security = @SecurityRequirement(name = "Authorization"))
//    @PutMapping("/{notificationId}/read")
//    public ResponseEntity<ApiResponse<Void>> markNotificationAsRead(
//            @PathVariable Long notificationId) {
//
//        User user = userAuthCommandService.getAuthenticatedUserInfo();
//
//        ApiResponse<Void> response = notificationService.markNotificationAsRead(user, notificationId);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter streamNotifications() {
//        User user = userAuthCommandService.getAuthenticatedUserInfo();
//        return notificationService.streamNotifications(user);
//    }
//}
