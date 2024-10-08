package com.example.we_save.domain.post.controller.response;

import com.example.we_save.apiPayload.util.RegionUtil;
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
    private long regionId;
    private double distance;
    private int hearts;
    private String regionName;
    private LocalDateTime createAt;
    private String title;

    private String imageUrl;

    private String categoryName;

    public static NearPostHomeResponseDto of(Post post, long regionId, double distance) {

        String imageUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getFilePath();

        return NearPostHomeResponseDto.builder()
                .postId(post.getId())
                .regionId(regionId)
                .distance(Math.round(distance * 10) / 10.0)
                .hearts(post.getHearts())
                .regionName(RegionUtil.extractRegionAfterSecondSpace(post.getPostRegionName()))
                .createAt(post.getCreateAt())
                .title(post.getTitle())
                .imageUrl(imageUrl)
                .categoryName(post.getCategory().getValue()).build();
    }
}
