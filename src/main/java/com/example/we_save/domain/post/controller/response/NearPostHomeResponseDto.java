package com.example.we_save.domain.post.controller.response;

import com.example.we_save.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NearPostHomeResponseDto {

    private long postId;
    private long areaId;
    private double distance;
    private int hearts;
    private LocalDateTime createAt;

    private String imageUrl;

    private long categoryId;
    private String categoryName;

    // TODO: 정적메서드 of 구현
    public static NearPostHomeResponseDto of(Post post, double distance, String categoryName) {

        return NearPostHomeResponseDto.builder()
                .postId(post.getId())
                // TODO: AreaID
                .distance(distance)
                .hearts(post.getHearts())
                .createAt(post.getCreatedAt())
                .imageUrl(post.getImages().isEmpty() ?  null : post.getImages().get(0))
                .categoryId(post.getCategoryId())
                .categoryName(categoryName).build();
    }
}
