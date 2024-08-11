package com.example.we_save.domain.user.service;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.user.controller.response.UserPostResponseDto;
import com.example.we_save.domain.user.entity.Block;
import com.example.we_save.domain.user.entity.User;

import java.util.List;

public interface UserService {

    ApiResponse<List<UserPostResponseDto>> getMyPosts();
    ApiResponse<Void> updatePostCompleted(long postId);
    ApiResponse<Void> deletePost(long postId);
    Block addBlock(User user, long targetId);
}