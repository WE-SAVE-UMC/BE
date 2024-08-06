package com.example.we_save.domain.post.controller.response;

import com.example.we_save.domain.comment.controller.response.CommentDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDtoWithComments {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private String status;
    private double longitude;
    private double latitude;
    private String postRegionName;
    private int hearts;
    private int dislikes;
    private int comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> images;
    private List<CommentDto> commentsList;
}
