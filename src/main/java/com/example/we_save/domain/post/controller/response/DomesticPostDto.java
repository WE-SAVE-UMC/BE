package com.example.we_save.domain.post.controller.response;

import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.post.entity.Category;
import com.example.we_save.domain.post.entity.Post;
import lombok.*;

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

    public static DomesticPostDto of(Post post) {
        return DomesticPostDto.builder()
                .postId(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent().substring(0, Math.min(20, post.getContent().length())) + "...")
                .postRegionName(RegionUtil.extractRegionAfterSecondSpace(post.getPostRegionName()))
                .hearts(post.getHearts())
                .dislikes(post.getDislikes())
                .image(post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl())
                .imageCount(post.getImages().size())
                .createdAt(post.getCreateAt().toString())
                .build();
    }

    public String getCategory() {
        return category.getValue();
    }
}
