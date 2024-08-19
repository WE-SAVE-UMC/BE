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
    private String buttonType;
    private String postTitle;
    private String notificationType;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationDto of(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getId())
                .postId(notification.getPostId())
                .buttonType(notification.getButtonType())
                .postTitle(notification.getPostTitle())
                .notificationType(notification.getNotificationType())
                .createdAt(notification.getTimestamp())
                .build();
    }

}
