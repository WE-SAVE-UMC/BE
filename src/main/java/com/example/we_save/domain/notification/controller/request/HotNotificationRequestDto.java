package com.example.we_save.domain.notification.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotNotificationRequestDto {
    private String hotPostTitle;  // 핫 상태에 도달한 게시물의 제목
}