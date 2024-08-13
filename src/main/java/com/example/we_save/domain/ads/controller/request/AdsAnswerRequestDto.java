package com.example.we_save.domain.ads.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdsAnswerRequestDto {

    @NotNull(message = "Ad ID must not be null.")
    private Long adId; // 광고 ID

    @NotNull(message = "Selected option ID must not be null")
    private Long selectedOptionId; // 사용자가 선택한 선택지 ID
}
