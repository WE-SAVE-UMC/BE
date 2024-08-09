package com.example.we_save.domain.user.service;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.entity.PostStatus;
import com.example.we_save.domain.post.repository.PostRepository;
import com.example.we_save.domain.user.controller.response.UserPostResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAuthCommandService userAuthService;
    private final PostRepository postRepository;

    @Override
    public ApiResponse<List<UserPostResponseDto>> getMyPosts() {

        long userId = userAuthService.getAuthenticatedUserInfo().getId();

        List<Post> posts = postRepository.findAllByUser(userId);

        List<UserPostResponseDto> userDtos = posts.stream()
                .map(UserPostResponseDto::of)
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(userDtos);
    }

    @Override
    @Transactional
    public ApiResponse<Void> updatePostCompleted(long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        post.setStatus(PostStatus.COMPLETED);

        postRepository.save(post);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    public ApiResponse<Void> deletePost(long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        postRepository.delete(post);

        return ApiResponse.onDeleteSuccess(null);
    }
}
