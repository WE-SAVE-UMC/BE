package com.example.we_save.domain.post.controller.response;

import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.post.entity.Category;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.entity.PostStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomesticPostDto {
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
    private boolean completed;

    public static DomesticPostDto of(Post post, List<Comment> comments) {
        // 댓글 이미지의 개수를 합산
        int commentImageCount = comments.stream()
                .mapToInt(comment -> comment.getImages().size())
                .sum();

        return DomesticPostDto.builder()
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
                .completed(post.getStatus() == PostStatus.COMPLETED)
                .build();
    }

    public String getCategory() {
        return category.getValue();
    }
}
