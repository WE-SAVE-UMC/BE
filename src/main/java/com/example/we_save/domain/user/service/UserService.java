package com.example.we_save.domain.user.service;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.user.controller.response.UserCommentResponseDto;
import com.example.we_save.domain.user.controller.response.UserPostResponseDto;

import java.util.List;

public interface UserService {

    ApiResponse<List<UserPostResponseDto>> getMyPosts();
    ApiResponse<Void> updatePostCompleted(long postId);
    ApiResponse<Void> deletePost(long postId);
    ApiResponse<List<UserCommentResponseDto>> getMyComments();
}
