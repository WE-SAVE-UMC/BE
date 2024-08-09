package com.example.we_save.domain.user.service;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.repository.PostRepository;
import com.example.we_save.domain.user.controller.response.UserPostResponseDto;
import com.example.we_save.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
