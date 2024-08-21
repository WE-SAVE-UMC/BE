package com.example.we_save.domain.ads.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.ads.controller.request.AdsRequestDto;
import com.example.we_save.domain.ads.controller.request.AdsAnswerRequestDto;
import com.example.we_save.domain.ads.controller.response.AdsAnswerResponseDto;
import com.example.we_save.domain.ads.controller.response.AdsResponseDto;
import com.example.we_save.domain.ads.entity.Ads;
import com.example.we_save.domain.ads.entity.AdsOption;
import com.example.we_save.domain.ads.repository.AdsRepository;
import com.example.we_save.domain.ads.repository.AdsOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;

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
        adsOptionRepository.deleteByAdsId(adId);

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

    @Override
    @Transactional
    public ApiResponse<AdsResponseDto> getRandomAd() {

        List<Ads> adsList = adsRepository.findAll();

        if (adsList.isEmpty()) {
            throw new EntityNotFoundException("광고를 찾을 수 없습니다.");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(adsList.size());
        Ads randomAd = adsList.get(randomIndex);

        AdsResponseDto responseDto = AdsResponseDto.builder()
                .adId(randomAd.getId())
                .question(randomAd.getQuestion())
                .options(
                        randomAd.getOptions().stream().map(option ->
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

        return ApiResponse.onGetSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<AdsAnswerResponseDto> submitAnswer(AdsAnswerRequestDto answerRequestDto) {
        Ads ads = adsRepository.findById(answerRequestDto.getAdId())
                .orElseThrow(() -> new EntityNotFoundException("광고를 찾을 수 없습니다."));

        AdsOption selectedOption = adsOptionRepository.findById(answerRequestDto.getSelectedOptionId())
                .orElseThrow(() -> new EntityNotFoundException("선택된 옵션을 찾을 수 없습니다."));

        if (selectedOption.getAds() == null || selectedOption.getAds().getId() != ads.getId()) {
            throw new IllegalArgumentException("옵션이 해당 광고와 일치하지 않습니다.");
        }

        AdsAnswerResponseDto responseDto = AdsAnswerResponseDto.builder()
                .isCorrect(selectedOption.isCorrect())
                .correctMessage(selectedOption.isCorrect() ? selectedOption.getResponseText() : null)
                .incorrectMessage(!selectedOption.isCorrect() ? selectedOption.getResponseText() : null)
                .imageUrl(selectedOption.getImageUrl())
                .build();

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }

    @Override
    public ApiResponse<AdsResponseDto> getAd(Long quizId) {

        Ads randomAd = adsRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("광고를 찾을 수 없습니다."));


        AdsResponseDto responseDto = AdsResponseDto.builder()
                .adId(randomAd.getId())
                .question(randomAd.getQuestion())
                .options(
                        randomAd.getOptions().stream().map(option ->
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

        return ApiResponse.onGetSuccess(responseDto);
    }
}