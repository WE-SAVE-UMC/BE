package com.example.we_save.domain.notification.controller.response;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long notificationId;
    private Long userId;
    private Long postId;
    private Long commentId;
    private String createdAt;
    private String title;
    private boolean isRead;
    private String content;
    private String buttonType;

    public NotificationResponseDto(Long notificationId, String title, LocalDateTime createdAt, boolean isRead) {
        this.notificationId = notificationId;
        this.title = title;
        this.createdAt = createdAt.toString();
        this.isRead = isRead;
    }

    public NotificationResponseDto(Long notificationId, Long userId, Long postId, Long commentId, String content, LocalDateTime createdAt, boolean isRead) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt.toString();
        this.isRead = isRead;
    }

    public NotificationResponseDto(Long notificationId, Long userId, Long postId, String buttonType, LocalDateTime createdAt, boolean isRead) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.postId = postId;
        this.buttonType = buttonType;
        this.createdAt = createdAt.toString();
        this.isRead = isRead;
    }
}
