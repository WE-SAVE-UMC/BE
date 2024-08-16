package com.example.we_save.domain.notification.controller.response;

import com.example.we_save.domain.notification.entity.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private Long notificationId;
    private Long postId;
    private Long commentId;
    private String buttonType;
    private String title;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.postId = notification.getPost() != null ? notification.getPost().getId() : null;
        this.commentId = notification.getCommentId();
        this.buttonType = notification.getButtonType();
        this.title = notification.getTitle();
        this.content = notification.getContent();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }
}
