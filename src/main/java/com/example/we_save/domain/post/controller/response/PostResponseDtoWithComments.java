package com.example.we_save.domain.post.controller.response;

import com.example.we_save.domain.comment.controller.response.CommentDto;
import com.example.we_save.domain.post.entity.Category;
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
    private String nickname;
    private Category category;
    private String title;
    private String content;
    private double longitude;
    private double latitude;
    private String postRegionName;
    private Boolean userReaction;
    private int hearts;
    private int dislikes;
    private int comments;
    private int imageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> images;
    private List<CommentDto> commentsList;

    public String getCategory() {
        return category.getValue();
    }
}
