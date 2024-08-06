package com.example.we_save.domain.home.controller.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.home.controller.response.HomeResponseDto;

public interface HomeService {

    ApiResponse<HomeResponseDto> showHomePage();
}
