package com.example.we_save.domain.home.controller.response;

import com.example.we_save.domain.post.controller.response.HotPostHomeResponseDto;
import com.example.we_save.domain.post.controller.response.NearPostHomeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class HomeResponseDto {
    private List<NearPostHomeResponseDto> postDtos;
    private List<HotPostHomeResponseDto> hostPostDtos;
    // TODO: 광고 DTO 추가
}
