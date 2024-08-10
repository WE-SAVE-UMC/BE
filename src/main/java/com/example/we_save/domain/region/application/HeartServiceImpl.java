package com.example.we_save.domain.region.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.region.controller.request.RegionNameRequestDto;
import com.example.we_save.domain.region.controller.response.HeartRegionResponseDto;
import com.example.we_save.domain.region.controller.response.RegionIdResponseDto;
import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import com.example.we_save.domain.region.entity.HeartRegion;
import com.example.we_save.domain.region.repository.EupmyeondongRepository;
import com.example.we_save.domain.region.repository.HeartRegionRepository;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.service.UserAuthCommandService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {

    private final HeartRegionRepository heartRegionRepository;
    private final EupmyeondongRepository eupmyeondongRepository;
    private final UserAuthCommandService userAuthService;
    private final RegionUtil regionUtil;

    @Override
    public ApiResponse<List<HeartRegionResponseDto>> lookupHeartRegion() {

        User user = userAuthService.getAuthenticatedUserInfo();

        List<HeartRegion> heartRegions = heartRegionRepository.findAllByUser(user);

        List<EupmyeondongRegion> eupmyeondongRegions = heartRegions.stream()
                .map(HeartRegion::getRegion)
                .collect(Collectors.toList());

        List<HeartRegionResponseDto> regionDtos = eupmyeondongRegions.stream()
                .map(HeartRegionResponseDto::of)
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(regionDtos);
    }

    @Override
    @Transactional
    public ApiResponse<RegionIdResponseDto> insertHeartRegion(RegionNameRequestDto regionNameDto) {

        User user = userAuthService.getAuthenticatedUserInfo();

        long regionId = regionUtil.convertRegionNameToRegionId(regionNameDto.getRegionName());

        EupmyeondongRegion region = eupmyeondongRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));

        List<HeartRegion> heartRegions = heartRegionRepository.findAllByUser(user);
        if (heartRegions.size() >= 2) {
            throw new IllegalArgumentException("사용자는 최대 2개의 관심 지역만 가질 수 있습니다.");
        }
        if (heartRegionRepository.findByUserAndRegion(user, region).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 관심지역입니다");
        }

        HeartRegion heartRegion = HeartRegion.builder()
                .user(user)
                .region(region)
                .build();

        heartRegionRepository.save(heartRegion);

        RegionIdResponseDto RegionIdDto = RegionIdResponseDto.builder()
                .regionId(region.getId())
                .build();

        return ApiResponse.onPostSuccess(RegionIdDto, SuccessStatus._POST_OK);
    }

    @Override
    public ApiResponse<Void> deleteHeartRegion(long regionId) {

        User user = userAuthService.getAuthenticatedUserInfo();

        EupmyeondongRegion region = eupmyeondongRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));

        HeartRegion heartRegion = heartRegionRepository.findByUserAndRegion(user, region)
                .orElseThrow(() -> new EntityNotFoundException("HotRegion not found"));

        heartRegionRepository.delete(heartRegion);

        return ApiResponse.onDeleteSuccess(null);
    }
}