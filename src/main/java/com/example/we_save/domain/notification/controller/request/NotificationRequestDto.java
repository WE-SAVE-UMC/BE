package com.example.we_save.domain.notification.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {

    private Long postId;
    private String postTitle;
    private String notificationType;
    private String username;
    private String buttonType;
    private Integer buttonTotalCount;
}



//import com.example.we_save.domain.post.entity.Category;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import java.time.LocalDateTime;
//import lombok.*;
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class NotificationRequestDto {
//
//    @NotNull(message = "Post ID must not be empty")
//    private Long postId;
//
//    @NotEmpty(message = "Title name must not be empty")
//    private String title;
//
//    private Long commentId;
//    private String content;
//    private String authorName;
//
//    private String buttonType;
//
//    private Integer confirmCount;
//
//    private LocalDateTime expiryTime;
//}
