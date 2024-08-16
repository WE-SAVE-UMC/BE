package com.example.we_save.domain.notification.controller.request;

import com.example.we_save.domain.post.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {

    @NotNull(message = "Post ID must not be empty")
    private Long postId;

    @NotEmpty(message = "Title name must not be empty")
    private String title;

    private Long commentId;
    private String content;
    private String commenterName;

    private String buttonType;

    private Integer confirmCount;

    private LocalDateTime expiryTime;
}
