package com.example.we_save.domain.region.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.region.controller.response.HeartRegionResponseDto;

import java.util.List;

public interface HeartService {

    ApiResponse<List<HeartRegionResponseDto>> lookupHeartRegion();
    ApiResponse<Void> insertHeartRegion(long regionId);
    ApiResponse<Void> deleteHeartRegion(long regionId);
}
