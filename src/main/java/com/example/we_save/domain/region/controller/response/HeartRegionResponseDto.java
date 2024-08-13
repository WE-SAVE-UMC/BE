package com.example.we_save.domain.region.controller.response;

import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeartRegionResponseDto {

    private String regionName;
    private long regionId;

    public static HeartRegionResponseDto of(EupmyeondongRegion region) {
        return HeartRegionResponseDto.builder()
                .regionName(region.getRegionFullName())
                .regionId(region.getId())
                .build();
    }
}
