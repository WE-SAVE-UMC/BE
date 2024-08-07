package com.example.we_save.domain.home.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.home.controller.request.HomeLocationRequestDto;
import com.example.we_save.domain.home.controller.response.HomeResponseDto;
import com.example.we_save.domain.post.controller.response.HotPostHomeResponseDto;
import com.example.we_save.domain.post.controller.response.NearPostHomeResponseDto;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final PostRepository postRepository;
    private final RegionUtil regionUtil;

    private final int LIMIT = 10;
    private final int RECENT = 0;
    private final int TOP = 1;
    private final int DISTANCE = 2;

    @Override
    public ApiResponse<HomeResponseDto> showHomePage(HomeLocationRequestDto locationDto) {

        List<NearPostHomeResponseDto> nearPosts = getNearDisasterPages(locationDto, LIMIT, RECENT);
        List<HotPostHomeResponseDto> hotPosts = getHotDisasterPages(locationDto, LIMIT);

        HomeResponseDto homeResponseDto = HomeResponseDto.builder()
                .postDtos(nearPosts)
                .hostPostDtos(hotPosts)
                .build();

        return ApiResponse.onGetSuccess(homeResponseDto);
    }

    @Override
    public ApiResponse<List<NearPostHomeResponseDto>> showRecentNearPosts(HomeLocationRequestDto locationDto) {

        return ApiResponse.onGetSuccess(getNearDisasterPages(locationDto, LIMIT, RECENT));
    }

    @Override
    public ApiResponse<List<NearPostHomeResponseDto>> showTopNearPosts(HomeLocationRequestDto locationDto) {

        return ApiResponse.onGetSuccess(getNearDisasterPages(locationDto, LIMIT, TOP));
    }

    @Override
    public ApiResponse<List<NearPostHomeResponseDto>> showDistanceNearPosts(HomeLocationRequestDto locationDto) {

        return ApiResponse.onGetSuccess(getNearDisasterPages(locationDto, LIMIT, DISTANCE));
    }

    private List<NearPostHomeResponseDto> getNearDisasterPages(HomeLocationRequestDto locationDto, int limit, int type) {

        // 현재 시간 기준으로 최근 3일간의 게시물 조회
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);

        // 페이지 요청 설정
        Pageable pageable = PageRequest.of(0, limit);

        // 현재 지역 ID 가져오기
        long regionId = regionUtil.convertRegionNameToRegionId(locationDto.getRegionName());
        double latitude = locationDto.getLatitude();
        double longitude = locationDto.getLongitude();

        // 최근 게시물 조회
        List<Post> posts;
        if (type == RECENT) {
            posts = postRepository.findRecentPosts(startDate, regionId, pageable);
        } else if (type == TOP) {
            posts = postRepository.findTopNearPosts(startDate, regionId, pageable);
        } else if (type == DISTANCE) {
            posts = postRepository.findDistanceNearPosts(regionId, longitude, latitude, pageable);
        } else {
            throw new IllegalArgumentException();
        }

        return posts.stream().map((post) -> {

            // 각 게시물 위치와의 거리 가져오기
            double distanceToPost = calculateDistanceToPost(post, latitude, longitude);
            return NearPostHomeResponseDto.of(post, regionId, distanceToPost);
        }).collect(Collectors.toList());
    }

    private List<HotPostHomeResponseDto> getHotDisasterPages(HomeLocationRequestDto locationDto, int limit) {

        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        Pageable pageable = PageRequest.of(0, limit);

        double latitude = locationDto.getLatitude();
        double longitude = locationDto.getLongitude();

        List<Post> posts = postRepository.findTopPosts(startDate, pageable);

        return posts.stream().map((post) -> {

            double distanceToPost = calculateDistanceToPost(post, latitude, longitude);
            return HotPostHomeResponseDto.of(post, distanceToPost);
        }).collect(Collectors.toList());
    }

    private double calculateDistanceToPost(Post post, double latitude, double longitude) {

        double postLatitude = post.getLatitude();
        double postLongitude = post.getLongitude();
        return RegionUtil.calculateDistanceBetweenCoordinates(postLatitude, postLongitude, latitude, longitude);
    }
}
