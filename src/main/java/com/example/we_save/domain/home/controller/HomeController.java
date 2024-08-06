package com.example.we_save.domain.home.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.home.controller.application.HomeService;
import com.example.we_save.domain.home.controller.response.HomeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    ResponseEntity<ApiResponse<HomeResponseDto>> showHomePage() {

        return ResponseEntity.ok(homeService.showHomePage());
    }

    // TODO: 확인순

    // TODO: 최신순
}
