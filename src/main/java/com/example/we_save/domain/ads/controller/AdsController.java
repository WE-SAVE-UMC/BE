package com.example.we_save.domain.ads.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.ads.application.AdsService;
import com.example.we_save.domain.ads.controller.request.AdsAnswerRequestDto;
import com.example.we_save.domain.ads.controller.request.AdsRequestDto;
import com.example.we_save.domain.ads.controller.response.AdsAnswerResponseDto;
import com.example.we_save.domain.ads.controller.response.AdsResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ads")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdsResponseDto>> createAds(
            @RequestBody AdsRequestDto adsRequestDto) {
        ApiResponse<AdsResponseDto> responseDto = adsService.createAds(adsRequestDto);
        return ResponseEntity.status(201).body(responseDto);
    } // 광고 생성


    @PutMapping("/{adId}")
    public ResponseEntity<ApiResponse<AdsResponseDto>> updateAds(
            @PathVariable("adId") Long adId,
            @RequestBody AdsRequestDto adsRequestDto) {
        ApiResponse<AdsResponseDto> responseDto = adsService.updateAds(adId, adsRequestDto);
        return ResponseEntity.ok(responseDto);
    } // 광고 수정

    @DeleteMapping("/{adId}")
    public ResponseEntity<ApiResponse<Void>> deleteAds(
            @PathVariable("adId") long adId) {
        ApiResponse<Void> responseDto = adsService.deleteAds(adId);
        return ResponseEntity.status(204).body(responseDto);
    }  // 광고 삭제

    @GetMapping("/quiz")
    public ResponseEntity<ApiResponse<AdsResponseDto>> getRandomAd() {
        ApiResponse<AdsResponseDto> responseDto = adsService.getRandomAd();
        return ResponseEntity.ok(responseDto);
    } // 랜덤 광고 가져오기

    @PostMapping("/answer")
    public ResponseEntity<ApiResponse<AdsAnswerResponseDto>> submitAnswer(
            @RequestBody AdsAnswerRequestDto adsAnswerRequestDto) {
        ApiResponse<AdsAnswerResponseDto> responseDto = adsService.submitAnswer(adsAnswerRequestDto);
        return ResponseEntity.ok(responseDto);
    } // 광고 응답 제출

    @GetMapping("/quizs")
    public ResponseEntity<ApiResponse<AdsResponseDto>> getRandomAd(
            @PathVariable("id") Long id
    ) {
        ApiResponse<AdsResponseDto> responseDto = adsService.getAd(id);
        return ResponseEntity.ok(responseDto);
    }
}