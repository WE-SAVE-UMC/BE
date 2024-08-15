package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.post.controller.request.NearbyPostRequestDto;
import com.example.we_save.domain.post.controller.request.PostRequestDto;
import com.example.we_save.domain.post.controller.response.DomesticPostDto;
import com.example.we_save.domain.post.controller.response.NearbyPostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDtoWithComments;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.user.entity.User;

import java.util.List;

public interface PostService {
    Post createPost(PostRequestDto postRequestDto, User user);
    Post updatePost(Long postId, PostRequestDto postRequestDto, User user);
    ApiResponse<PostResponseDto> deletePost(Long postId);
    ApiResponse<PostResponseDtoWithComments> getPost(Long postId, User user);
    ApiResponse<Void> reportPost(Long postId, User user);
    ApiResponse<Void> toggleHeart(Long postId, User user);
    ApiResponse<Void> toggleDislike(Long postId, User user);
    ApiResponse<NearbyPostResponseDto> getRecentNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
    ApiResponse<NearbyPostResponseDto> getTopNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
    ApiResponse<NearbyPostResponseDto> getDistanceNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);
    ApiResponse<List<DomesticPostDto>> getRecentDomesticPosts(boolean excludeCompleted);
    ApiResponse<List<DomesticPostDto>> getTopDomesticPosts(boolean excludeCompleted);
    ApiResponse<Void> changeToPostCompleted(long postId);

    ApiResponse<NearbyPostResponseDto> searchNearbyPosts(String query, String sortBy, NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted);

    ApiResponse<List<DomesticPostDto>> searchDomesticPosts(String query, String sortBy, int page, boolean excludeCompleted);

    ApiResponse<DomesticPostDto> searchDomesticPosts(String query, String sortBy, DomesticPostDto domesticPostDto, int page, boolean excludeCompleted);
}