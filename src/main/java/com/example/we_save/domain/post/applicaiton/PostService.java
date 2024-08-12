package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.post.controller.request.NearbyPostRequestDto;
import com.example.we_save.domain.post.controller.request.PostRequestDto;
import com.example.we_save.domain.post.controller.response.NearbyPostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDtoWithComments;

public interface PostService {
    ApiResponse<PostResponseDto> createPost(PostRequestDto postRequestDto);
    ApiResponse<PostResponseDto> updatePost(Long postId, PostRequestDto postRequestDto);
    ApiResponse<PostResponseDto> deletePost(Long postId);
    ApiResponse<PostResponseDtoWithComments> getPost(Long postId);
    ApiResponse<Void> reportPost(Long postId, Long userId);
    ApiResponse<Void> toggleHeart(Long postId, Long userId);
    ApiResponse<Void> toggleDislike(Long postId, Long userId);
    ApiResponse<NearbyPostResponseDto> getRecentNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
    ApiResponse<NearbyPostResponseDto> getTopNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
    ApiResponse<NearbyPostResponseDto> getDistanceNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
}
