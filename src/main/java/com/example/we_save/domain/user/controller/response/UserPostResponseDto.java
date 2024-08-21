package com.example.we_save.domain.user.controller.response;

import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.post.entity.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserPostResponseDto {

    private long postId;
    private String title;
    private String status;
    private String category;
    private String regionName;
    private LocalDateTime createAt;
    private String imageUrl;

    public static UserPostResponseDto of(Post post) {

        String imageUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getFilePath();

        return UserPostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .status(post.getStatus().toString())
                .category(post.getCategory().toString())
                .regionName(RegionUtil.extractRegionAfterSecondSpace(post.getPostRegionName()))
                .createAt(post.getCreateAt())
                .imageUrl(imageUrl)
                .build();
    }
}
