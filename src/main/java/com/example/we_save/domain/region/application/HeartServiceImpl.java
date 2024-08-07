package com.example.we_save.domain.region.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.region.controller.response.HeartRegionResponseDto;
import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import com.example.we_save.domain.region.entity.HeartRegion;
import com.example.we_save.domain.region.repository.EupmyeondongRepository;
import com.example.we_save.domain.region.repository.HeartRegionRepository;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {

    private final HeartRegionRepository heartRegionRepository;
    private final EupmyeondongRepository eupmyeondongRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<List<HeartRegionResponseDto>> lookupHeartRegion() {

        User user = getUserByJWT();

        List<HeartRegion> heartRegions = heartRegionRepository.findAllByUser(user);

        List<Long> heartRegionIds = heartRegions.stream()
                .map(HeartRegion::getId)
                .collect(Collectors.toList());

        List<EupmyeondongRegion> eupmyeondongRegions = eupmyeondongRepository.findAllByHeartRegionIdIn(heartRegionIds);

        List<HeartRegionResponseDto> regionDtos = eupmyeondongRegions.stream()
                .map(HeartRegionResponseDto::of)
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(regionDtos);
    }

    @Override
    @Transactional
    public ApiResponse<Void> insertHeartRegion(long regionId) {

        User user = getUserByJWT();

        EupmyeondongRegion region = eupmyeondongRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));

        List<HeartRegion> heartRegions = heartRegionRepository.findAllByUser(user);
        if (heartRegions.size() >= 2) {
            throw new IllegalArgumentException("사용자는 최대 2개의 관심 지역만 가질 수 있습니다.");
        }

        HeartRegion heartRegion = HeartRegion.builder()
                .user(user)
                .region(region)
                .build();

        heartRegionRepository.save(heartRegion);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    public ApiResponse<Void> deleteHeartRegion(long regionId) {

        User user = getUserByJWT();

        EupmyeondongRegion region = eupmyeondongRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));

        HeartRegion heartRegion = heartRegionRepository.findByUserAndRegion(user, region)
                .orElseThrow(() -> new EntityNotFoundException("HotRegion not found"));

        heartRegionRepository.delete(heartRegion);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    private User getUserByJWT() {

        // TODO: JWT 헤더에서 현재 로그인한 User Id 가져오기 -> 함수로 변환
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        long userId = Long.parseLong(userIdStr);

        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
