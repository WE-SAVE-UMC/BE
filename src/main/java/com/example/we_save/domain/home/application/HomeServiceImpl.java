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

    @Override
    public ApiResponse<HomeResponseDto> showHomePage(HomeLocationRequestDto locationDto) {

        List<NearPostHomeResponseDto> nearPosts = getNearDisasterPages(locationDto, 10);
        List<HotPostHomeResponseDto> hotPosts = getHotDisasterPages();

        HomeResponseDto homeResponseDto = HomeResponseDto.builder()
                .postDtos(nearPosts)
                .hostPostDtos(hotPosts)
                .build();

        return ApiResponse.onGetSuccess(homeResponseDto);
    }

    private List<NearPostHomeResponseDto> getNearDisasterPages(HomeLocationRequestDto locationDto, int limit) {

        // 현재 시간 기준으로 최근 3일간의 게시물 조회
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);

        // 페이지 요청 설정
        Pageable pageable = PageRequest.of(0, limit);

        // 현재 지역 ID 가져오기
        long regionId = regionUtil.convertRegionNameToRegionId(locationDto.getRegionName());
        double latitude = locationDto.getLatitude();
        double longitude = locationDto.getLongitude();

        // 최근 게시물 조회
        List<Post> posts = postRepository.findRecentPosts(startDate, regionId, pageable);

        return posts.stream().map((post) -> {

            // 각 게시물 위치와의 거리 가져오기
            double distanceToPost = calculateDistanceToPost(post, latitude, longitude);
            return NearPostHomeResponseDto.of(post, regionId, distanceToPost);
        }).collect(Collectors.toList());
    }

    private List<HotPostHomeResponseDto> getHotDisasterPages() {

        return null;
    }

    private double calculateDistanceToPost(Post post, double latitude, double longitude) {

        double postLatitude = post.getLatitude();
        double postLongitude = post.getLongitude();
        return RegionUtil.calculateDistanceBetweenCoordinates(postLatitude, postLongitude, latitude, longitude);
    }
}
