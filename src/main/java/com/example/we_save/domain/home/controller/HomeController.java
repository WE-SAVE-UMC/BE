package com.example.we_save.domain.home.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.countermeasure.controller.response.CountermeasureResponseDto;
import com.example.we_save.domain.home.application.HomeService;
import com.example.we_save.domain.home.controller.request.HomeLocationRequestDto;
import com.example.we_save.domain.home.controller.response.HomeResponseDto;
import com.example.we_save.domain.post.controller.response.NearPostHomeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "메인페이지 게시글 조회")
    @PostMapping
    ResponseEntity<ApiResponse<HomeResponseDto>> showHomePage(@RequestBody HomeLocationRequestDto locationDto) {

        return ResponseEntity.ok(homeService.showHomePage(locationDto));
    }

    @Operation(summary = "메인페이지 내 근처 사건사고 확인순")
    @PostMapping("/sort/top")
    ResponseEntity<ApiResponse<List<NearPostHomeResponseDto>>> showTopNearPosts(@RequestBody HomeLocationRequestDto locationDto) {

        return ResponseEntity.ok(homeService.showTopNearPosts(locationDto));
    }

    @Operation(summary = "메인페이지 내 근처 사건사고 최신순")
    @PostMapping("/sort/recent")
    ResponseEntity<ApiResponse<List<NearPostHomeResponseDto>>> showRecentNearPosts(@RequestBody  HomeLocationRequestDto locationDto) {

        return ResponseEntity.ok(homeService.showRecentNearPosts(locationDto));
    }

    @Operation(summary = "메인페이지 내 근처 사건사고 거리순")
    @PostMapping("/sort/distance")
    ResponseEntity<ApiResponse<List<NearPostHomeResponseDto>>> showDistanceNearPosts(@RequestBody  HomeLocationRequestDto locationDto) {

        return ResponseEntity.ok(homeService.showDistanceNearPosts(locationDto));
    }

    @Operation(summary = "메인페이지 검색")
    @GetMapping("/search")
    ResponseEntity<ApiResponse<List<CountermeasureResponseDto>>> searchCountermeasures
            (@RequestParam(value = "keyword", required = false) String keyword) {

        // 검색어가 없을 경우 실패 응답 반환
        if (keyword == null || keyword.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.onFailure("400", "Keyword is missing", null));
        }

        return ResponseEntity.ok(homeService.findCountermeasuresByKeyword(keyword));
    }
}
