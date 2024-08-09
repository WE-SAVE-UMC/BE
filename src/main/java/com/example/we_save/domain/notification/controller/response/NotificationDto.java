package com.example.we_save.domain.notification.controller.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long notificationId;
    private Long userId;
    private Long postId;
    private Long commentId;
    private LocalDateTime createdAt;
}
