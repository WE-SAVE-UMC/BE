package com.example.we_save.domain.comment.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @NotNull(message = "PostId must not be empty")
    private Long postId;

    @NotNull(message = "UserId must not be empty")
    private Long userId;

    @NotEmpty(message = "Content must not be empty")
    private String content;
}