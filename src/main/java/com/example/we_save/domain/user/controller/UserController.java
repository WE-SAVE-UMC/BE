package com.example.we_save.domain.user.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.user.controller.response.UserPostResponseDto;
import com.example.we_save.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "마이페이지 게시물 조회")
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<UserPostResponseDto>>> getMyPosts() {

        return ResponseEntity.ok(userService.getMyPosts());
    }

    @Operation(summary = "마이페이지 게시물 상황종료 처리")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePostCompleted(@PathVariable("postId") long postId) {

        return ResponseEntity.ok(userService.updatePostCompleted(postId));
    }

    @Operation(summary = "마이페이지 게시물 삭제")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable("postId") long postId) {

        return ResponseEntity.ok(userService.deletePost(postId));
    }
}
