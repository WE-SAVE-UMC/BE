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
public class HotPostHomeResponseDto {

    private long postId;
    private String title;
    private String status;
    private long regionId;
    private String regionName;
    private double latitude;
    private double longitude;
    private String categoryName;
    private double distance;
    private int hearts;
    private LocalDateTime createAt;

    private String imageUrl;

    public static HotPostHomeResponseDto of(Post post, double distance) {

        String imageUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getFilePath();

        return HotPostHomeResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .status(post.getStatus().getValue())
                .regionId(post.getRegion().getId())
                .regionName(RegionUtil.extractRegionBeforeSecondSpace(post.getPostRegionName()))
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .categoryName(post.getCategory().getValue())
                .distance(Math.round(distance * 10) / 10.0)
                .hearts(post.getHearts())
                .createAt(post.getCreateAt())
                .imageUrl(imageUrl)
                .build();
    }
}
