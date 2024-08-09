package com.example.we_save.domain.user.controller.response;

import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.post.entity.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserPostResponseDto {

    long postId;
    String title;
    String status;
    String regionName;
    LocalDateTime createAt;
    String imageUrl;

    public static UserPostResponseDto of(Post post) {

        String imageUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();

        return UserPostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .status(post.getStatus().getValue())
                .regionName(RegionUtil.extractRegionAfterSecondSpace(post.getPostRegionName()))
                .createAt(post.getCreateAt())
                .imageUrl(imageUrl)
                .build();
    }
}
