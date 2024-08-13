package com.example.we_save.domain.ads.controller.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AdsAnswerResponseDto {

    private boolean isCorrect;
    private String correctMessage;
    private String incorrectMessage;
    private String imageUrl;
}
