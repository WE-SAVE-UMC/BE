package com.example.we_save.domain.comment.controller.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private Long postId;
    private Long userId;
    private String content;
    private List<String> images;
}
