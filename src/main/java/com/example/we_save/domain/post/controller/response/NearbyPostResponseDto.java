package com.example.we_save.domain.post.controller.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyPostResponseDto {
    private String userRegionName;
    private List<PostDto> postDTOs;


    public static NearbyPostResponseDto of(String userRegionName, List<PostDto> postDTOs) {
        return NearbyPostResponseDto.builder()
                .userRegionName(userRegionName)
                .postDTOs(postDTOs)
                .build();
    }

    public static NearbyPostResponseDto of(Long postId, String postTitle, String userRegionName) {
        return NearbyPostResponseDto.builder()
                .build();
    }
}