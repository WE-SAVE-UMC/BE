package com.example.we_save.domain.notification.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HourNotificationRequestDto {
    private Long postId;          // 알림을 생성할 게시물의 ID
}