package com.example.we_save.domain.ads.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.ads.controller.request.AdsAnswerRequestDto;
import com.example.we_save.domain.ads.controller.request.AdsRequestDto;
import com.example.we_save.domain.ads.controller.response.AdsAnswerResponseDto;
import com.example.we_save.domain.ads.controller.response.AdsResponseDto;
import jakarta.transaction.Transactional;

public interface AdsService {
    ApiResponse<AdsResponseDto> createAds(AdsRequestDto adsRequestDto);

    ApiResponse<AdsResponseDto> updateAds(Long adId, AdsRequestDto adsRequestDto);

    ApiResponse<Void> deleteAds(Long adId);

    ApiResponse<AdsResponseDto> getRandomAd();

    ApiResponse<AdsAnswerResponseDto> submitAnswer(AdsRequestDto answerRequestDto);

    @Transactional
    ApiResponse<AdsAnswerResponseDto> submitAnswer(AdsAnswerRequestDto answerRequestDto);
}
