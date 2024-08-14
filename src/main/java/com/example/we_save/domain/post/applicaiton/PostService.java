package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.post.controller.request.NearbyPostRequestDto;
import com.example.we_save.domain.post.controller.request.PostRequestDto;
import com.example.we_save.domain.post.controller.response.DomesticPostDto;
import com.example.we_save.domain.post.controller.response.NearbyPostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDtoWithComments;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.entity.PostImage;
import com.example.we_save.image.entity.Image;

import java.util.List;

public interface PostService {
    Post createPost(PostRequestDto postRequestDto);
    Post updatePostImages(List<PostImage> images, Post post);
    ApiResponse<PostResponseDto> updatePost(Long postId, PostRequestDto postRequestDto);
    ApiResponse<PostResponseDto> deletePost(Long postId);
    ApiResponse<PostResponseDtoWithComments> getPost(Long postId, Long userId);
    ApiResponse<Void> reportPost(Long postId, Long userId);
    ApiResponse<Void> toggleHeart(Long postId, Long userId);
    ApiResponse<Void> toggleDislike(Long postId, Long userId);
    ApiResponse<NearbyPostResponseDto> getRecentNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
    ApiResponse<NearbyPostResponseDto> getTopNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
    ApiResponse<NearbyPostResponseDto> getDistanceNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
    ApiResponse<List<DomesticPostDto>> getRecentDomesticPosts(boolean excludeCompleted);
    ApiResponse<List<DomesticPostDto>> getTopDomesticPosts(boolean excludeCompleted);
    ApiResponse<Void> changeToPostCompleted(long postId);
}
