package com.example.we_save.domain.region.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.region.controller.request.RegionNameRequestDto;
import com.example.we_save.domain.region.controller.response.HeartRegionResponseDto;
import com.example.we_save.domain.region.controller.response.RegionIdResponseDto;

import java.util.List;

public interface HeartService {

    ApiResponse<List<HeartRegionResponseDto>> lookupHeartRegion();
    ApiResponse<RegionIdResponseDto> insertHeartRegion(RegionNameRequestDto regionNameDto);
    ApiResponse<Void> deleteHeartRegion(long regionId);
}
