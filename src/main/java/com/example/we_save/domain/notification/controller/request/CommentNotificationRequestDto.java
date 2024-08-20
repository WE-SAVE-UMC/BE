package com.example.we_save.domain.notification.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentNotificationRequestDto {
    private Long postId;          // 댓글이 달린 게시물의 ID
    private String comment;       // 댓글 내용
    private String commentUserName; // 댓글을 작성한 사용자의 이름
}
