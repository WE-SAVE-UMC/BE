package com.example.we_save.domain.ads.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdsRequestDto {

    @NotEmpty(message = "Question must not be empty")
    private String question;
    // 광고 문제 (질문)

    @NotNull(message = "Options must not be null")
    private List<AdsOptionDto> options;

    // 광고 선택지 리스트

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdsOptionDto {
        @NotEmpty(message = "Text must not be empty")
        private String text;
        // 선택지 텍스트

        @NotNull(message = "IsCorrect must not be null")
        private Boolean isCorrect;
        // 정답 여부

        @NotEmpty(message = "ResponseText must not be empty")
        private String responseText;
        // 선택 후 보여줄 텍스트

        @NotEmpty(message = "ImageUrl must not be empty")
        private String imageUrl;
        // 선택 후 보여줄 이미지 URL

        @NotEmpty(message = "RedirectUrl must not be empty")
        private String redirectUrl;
        // 이미지를 클릭하면 이동할 URL
    }
}