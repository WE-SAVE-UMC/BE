package com.example.we_save.domain.comment.controller.request;

import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @NotEmpty(message = "PostId must not be empty")
    private Long postId;

    @NotEmpty(message = "UserId must not be empty")
    private Long userId;

    @NotEmpty(message = "Content must not be empty")
    private String content;

    private List<String> images;
}
