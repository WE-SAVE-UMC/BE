package com.example.we_save.domain.post.controller.response;

import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.post.entity.Category;
import com.example.we_save.domain.post.entity.Post;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long postId;
    private Category category;
    private String title;
    private String content;
    private String postRegionName;
    private int hearts;
    private int dislikes;
    private String image;
    private int imageCount;
    private String createdAt;
    private String distance;

    public static PostDto of(Post post, double distance, List<Comment> comments) {
        // 댓글 이미지의 개수를 합산
        int commentImageCount = comments.stream()
                .mapToInt(comment -> comment.getImages().size())
                .sum();

        return PostDto.builder()
                .postId(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent().substring(0, Math.min(20, post.getContent().length())) + "...")
                .postRegionName(RegionUtil.extractRegionAfterSecondSpace(post.getPostRegionName()))
                .hearts(post.getHearts())
                .dislikes(post.getDislikes())
                .image(post.getImages().isEmpty() ? null : post.getImages().get(0).getFilePath())
                .imageCount(commentImageCount) // 댓글 이미지 개수를 설정
                .createdAt(post.getCreateAt().toString())
                .distance(String.format("%.1f km", distance))  // 거리 값에 km 단위 추가
                .build();
    }

    public String getCategory() {
        return category.getValue();
    }
}

