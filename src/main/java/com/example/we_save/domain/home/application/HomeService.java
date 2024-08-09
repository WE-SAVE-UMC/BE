package com.example.we_save.domain.home.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.home.controller.request.HomeLocationRequestDto;
import com.example.we_save.domain.home.controller.response.HomeResponseDto;
import com.example.we_save.domain.post.controller.response.NearPostHomeResponseDto;

import java.util.List;

public interface HomeService {

    ApiResponse<HomeResponseDto> showHomePage(HomeLocationRequestDto locationDto);
    ApiResponse<List<NearPostHomeResponseDto>> showRecentNearPosts(HomeLocationRequestDto locationDto);
    ApiResponse<List<NearPostHomeResponseDto>> showTopNearPosts(HomeLocationRequestDto locationDto);
    ApiResponse<List<NearPostHomeResponseDto>> showDistanceNearPosts(HomeLocationRequestDto locationDto);
}
