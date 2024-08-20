package com.example.we_save.domain.notification.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ButtonNotificationRequestDto {
    private Long postId;           // 버튼이 눌린 게시물의 ID
    private String buttonType;     // 버튼의 유형 (확인했어요/허위예요)
    private int buttonCount;       // 해당 버튼이 눌린 총 개수
}
