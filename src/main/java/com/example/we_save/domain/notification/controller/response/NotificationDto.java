package com.example.we_save.domain.notification.controller.response;

import com.example.we_save.domain.notification.entity.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long notificationId;
    private Long postId;
    private Long commentId;
    private String buttonType;
    private String title;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationDto of(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getId())
                .postId(notification.getPost() != null ? notification.getPost().getId() : null)
                .commentId(notification.getCommentId())
                .buttonType(notification.getButtonType())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
