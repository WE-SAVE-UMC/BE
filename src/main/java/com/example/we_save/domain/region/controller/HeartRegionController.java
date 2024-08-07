package com.example.we_save.domain.region.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.region.application.HeartService;
import com.example.we_save.domain.region.controller.request.RegionNameRequestDto;
import com.example.we_save.domain.region.controller.response.HeartRegionResponseDto;
import com.example.we_save.domain.region.controller.response.RegionIdResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/heart")
@RequiredArgsConstructor
public class HeartRegionController {

    private final HeartService heartService;

    @Operation(summary = "관심지역 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<HeartRegionResponseDto>>> lookupHeartRegion() {

        return ResponseEntity.ok(heartService.lookupHeartRegion());
    }

    @Operation(summary = "관심지역 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<RegionIdResponseDto>> registerHeartRegion(@RequestBody RegionNameRequestDto regionNameDto) {

        return ResponseEntity.ok(heartService.insertHeartRegion(regionNameDto));
    }

    @Operation(summary = "관심지역 해제")
    @DeleteMapping("/{region-id}")
    public ResponseEntity<ApiResponse<Void>> deleteHeartRegion(@PathVariable("region-id") long regionId) {

        return ResponseEntity.ok(heartService.deleteHeartRegion(regionId));
    }
}
