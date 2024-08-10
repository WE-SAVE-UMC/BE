package com.example.we_save.domain.notification.controller.request;

import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.user.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class NotificationRequestDto {
    @NotNull(message = "UserId must not be empty")
    private Long userId;

    @NotNull(message = "PostId must not be empty")
    private Long postId;

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotNull(message = "Post created time must not be empty")
    private String postCreatedAt;

    private boolean isRead;
    private int confirmCount;
    private int falseReportCount;

    @NotNull(message = "CommentId must not be empty")
    private Long commentId;

    @NotEmpty(message = "Content must not be empty")
    private String content;
}
