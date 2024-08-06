package com.example.we_save.domain.home.controller.application;

import com.example.we_save.apiPayload.ApiResponse;
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
    // TODO: Category Repository 추가

    @Override
    public ApiResponse<HomeResponseDto> showHomePage() {

        // TODO: User의 현재위치 가져오기

        List<NearPostHomeResponseDto> nearPosts = getNearDisasterPages(10);
        List<HotPostHomeResponseDto> hotPosts = getHotDisasterPages();

        return null;
    }

    private List<NearPostHomeResponseDto> getNearDisasterPages(int limit) {

        // 현재 시간 기준으로 최근 3일간의 게시물 조회
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);

        // 페이지 요청 설정
        Pageable pageable = PageRequest.of(0, limit);

        // 1. 최근 게시물 조회
        List<Post> posts = postRepository.findRecentPosts(startDate, pageable);

        // 2. 게시물 ID 리스트 추출
        List<Long> postIDs = posts.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        // TODO: 3. 카테고리들을 한 번의 쿼리로 조회

        // TODO: 4. 카테고리를 Map으로 변환하여 빠르게 조회할 수 있게 함

        double distanceToPost = 1.0;
        // double distanceToPost = calculateDistanceToPost();


        return posts.stream().map((post) -> {
            // TODO: categoryName 추가
            return NearPostHomeResponseDto.of(post, distanceToPost, "temp");
        }).collect(Collectors.toList());
    }

    private List<HotPostHomeResponseDto> getHotDisasterPages() {

    }

    private double calculateDistanceToPost(double latitude, double longitude) {

        // TODO: 좌표값에 따라 거리 return 해주기
        return 0;
    }
}
