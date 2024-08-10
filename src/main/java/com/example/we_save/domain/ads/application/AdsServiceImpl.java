package com.example.we_save.domain.ads.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.ads.controller.request.AdsRequestDto;
import com.example.we_save.domain.ads.controller.response.AdsResponseDto;
import com.example.we_save.domain.ads.entity.Ads;
import com.example.we_save.domain.ads.entity.AdsOption;
import com.example.we_save.domain.ads.repository.AdsRepository;
import com.example.we_save.domain.ads.repository.AdsOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AdsServiceImpl implements AdsService {

    @Autowired
    private AdsRepository adsRepository;

    @Autowired
    private AdsOptionRepository adsOptionRepository;

    @Override
    @Transactional
    public ApiResponse<AdsResponseDto> createAds(AdsRequestDto adsRequestDto) {
        Ads ads = Ads.builder()
                .question(adsRequestDto.getQuestion())
                .build();

        Ads savedAds = adsRepository.save(ads);

        adsRequestDto.getOptions().forEach(optionDto -> {
            AdsOption option = AdsOption.builder()
                    .ads(savedAds)
                    .text(optionDto.getText())
                    .isCorrect(optionDto.getIsCorrect())
                    .responseText(optionDto.getResponseText())
                    .imageUrl(optionDto.getImageUrl())
                    .redirectUrl(optionDto.getRedirectUrl())
                    .build();

            adsOptionRepository.save(option);
        });

        AdsResponseDto responseDto = AdsResponseDto.builder()
                .adId(savedAds.getId())
                .question(savedAds.getQuestion())
                .options(
                        savedAds.getOptions().stream().map(option ->
                                AdsResponseDto.AdsOptionResponseDto.builder()
                                        .optionId(option.getId())
                                        .text(option.getText())
                                        .isCorrect(option.isCorrect())
                                        .responseText(option.getResponseText())
                                        .imageUrl(option.getImageUrl())
                                        .redirectUrl(option.getRedirectUrl())
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<AdsResponseDto> updateAds(Long adId, AdsRequestDto adsRequestDto) {
        Ads ads = adsRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("광고를 찾을 수 없습니다."));

        ads.setQuestion(adsRequestDto.getQuestion());
        adsOptionRepository.deleteAds(adId);

        adsRequestDto.getOptions().forEach(optionDto -> {
            AdsOption option = AdsOption.builder()
                    .ads(ads)
                    .text(optionDto.getText())
                    .isCorrect(optionDto.getIsCorrect())
                    .responseText(optionDto.getResponseText())
                    .imageUrl(optionDto.getImageUrl())
                    .redirectUrl(optionDto.getRedirectUrl())
                    .build();

            adsOptionRepository.save(option);
        });

        Ads updatedAds = adsRepository.save(ads);

        AdsResponseDto responseDto = AdsResponseDto.builder()
                .adId(updatedAds.getId())
                .question(updatedAds.getQuestion())
                .options(
                        updatedAds.getOptions().stream().map(option ->
                                AdsResponseDto.AdsOptionResponseDto.builder()
                                        .optionId(option.getId())
                                        .text(option.getText())
                                        .isCorrect(option.isCorrect())
                                        .responseText(option.getResponseText())
                                        .imageUrl(option.getImageUrl())
                                        .redirectUrl(option.getRedirectUrl())
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._PUT_OK);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteAds(Long adId) {
        Ads ads = adsRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("광고를 찾을 수 없습니다."));

        adsOptionRepository.deleteByAdsId(adId);
        adsRepository.delete(ads);

        return ApiResponse.onDeleteSuccess(null);
    }
}
