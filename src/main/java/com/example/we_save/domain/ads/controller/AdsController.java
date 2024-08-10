package com.example.we_save.domain.ads.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.ads.application.AdsService;
import com.example.we_save.domain.ads.controller.request.AdsRequestDto;
import com.example.we_save.domain.ads.controller.response.AdsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ads")
public class AdsController {

    @Autowired
    private AdsService adsService;

    @PostMapping
    public ResponseEntity<ApiResponse<AdsResponseDto>> createAds(
            @RequestBody AdsRequestDto adsRequestDto) {
        ApiResponse<AdsResponseDto> responseDto = adsService.createAds(adsRequestDto);
        return ResponseEntity.status(201).body(responseDto);
    }
}