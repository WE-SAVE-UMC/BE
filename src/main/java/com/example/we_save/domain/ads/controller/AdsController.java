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

    @PutMapping("/{adId}")
    public ResponseEntity<ApiResponse<AdsResponseDto>> updateAds(
            @PathVariable("adId") Long adId,
            @RequestBody AdsRequestDto adsRequestDto) {
        ApiResponse<AdsResponseDto> responseDto = adsService.updateAds(adId, adsRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{adId}")
    public ResponseEntity<ApiResponse<Void>> deleteAds(
            @PathVariable("adId") long adId) {
        ApiResponse<Void> responseDto = adsService.deleteAds(adId);
        return ResponseEntity.status(204).body(responseDto);
    }
}