package com.example.we_save.domain.home.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.home.controller.request.HomeLocationRequestDto;
import com.example.we_save.domain.home.controller.response.HomeResponseDto;

public interface HomeService {

    ApiResponse<HomeResponseDto> showHomePage(HomeLocationRequestDto locationDto);
}
