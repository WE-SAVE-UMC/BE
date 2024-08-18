package com.example.we_save.domain.countermeasure.controller.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class HomeSearchResponseDto {

    private List<String> tags;
    private List<CountermeasureResponseDto> countermeasureDtos;
}
