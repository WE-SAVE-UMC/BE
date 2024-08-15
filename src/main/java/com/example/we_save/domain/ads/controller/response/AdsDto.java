package com.example.we_save.domain.ads.controller.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdsDto {
    private Long adId; // 광고 ID
    private String question; // 광고 문제 (질문)
    private List<AdsOptionDto> options; // 광고 선택지 리스트
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간

    private Boolean isUpdated; // 광고가 수정되었는지 여부

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdsOptionDto {
        private Long optionId; // 선택지 ID
        private String text; // 선택지 텍스트
        private Boolean isCorrect; // 정답 여부
        private String responseText; // 선택 후 보여줄 텍스트
        private String imageUrl; // 선택 후 보여줄 이미지 URL
        private String redirectUrl; // 이미지를 클릭하면 이동할 URL
    }
}
