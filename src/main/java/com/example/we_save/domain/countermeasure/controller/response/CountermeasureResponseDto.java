package com.example.we_save.domain.countermeasure.controller.response;

import com.example.we_save.domain.countermeasure.entity.Countermeasure;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountermeasureResponseDto {

    private String title;
    private String mainContent;
    private String detailContent;

    public static CountermeasureResponseDto of(Countermeasure countermeasure) {

        return CountermeasureResponseDto.builder()
                .title(countermeasure.getTitle())
                .mainContent(countermeasure.getMainContent())
                .detailContent(countermeasure.getDetailContent())
                .build();
    }
}
