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

    private String title;  // 알림 제목
    private boolean isRead;  // 알림 읽음 상태

    public NotificationResponseDto(Long notificationId, String title, LocalDateTime createdAt, boolean isRead) {
        this.notificationId = notificationId;
        this.title = title;
        this.createdAt = createdAt.toString();  // LocalDateTime을 String으로 변환
        this.isRead = isRead;
    }
}
