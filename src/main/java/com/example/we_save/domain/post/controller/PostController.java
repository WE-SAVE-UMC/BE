package com.example.we_save.domain.post.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.post.applicaiton.PostService;
import com.example.we_save.domain.post.controller.request.NearbyPostRequestDto;
import com.example.we_save.domain.post.controller.request.PostRequestDto;
import com.example.we_save.domain.post.controller.response.NearbyPostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDtoWithComments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @RequestBody PostRequestDto postRequestDto) {
        ApiResponse<PostResponseDto> responseDto = postService.createPost(postRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost(
            @PathVariable("postId") Long postId,
            @RequestBody PostRequestDto postRequestDto) {
        ApiResponse<PostResponseDto> responseDto = postService.updatePost(postId, postRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> deletePost(
            @PathVariable("postId") Long postId) {
        ApiResponse<PostResponseDto> responseDto = postService.deletePost(postId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDtoWithComments>> getPost(
            @PathVariable("postId") Long postId){
        ApiResponse<PostResponseDtoWithComments> responseDto = postService.getPost(postId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/posts/{postId}/report")
    public ResponseEntity<ApiResponse<Void>> reportPost(
            @PathVariable("postId") Long postId,
            @RequestParam("userId") Long userId) {
        ApiResponse<Void> response = postService.reportPost(postId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/posts/{postId}/heart")
    public ResponseEntity<ApiResponse<Void>> toggleHeart(
            @PathVariable("postId") Long postId,
            @RequestParam("userId") Long userId) {
        ApiResponse<Void> response = postService.toggleHeart(postId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/posts/{postId}/dislike")
    public ResponseEntity<ApiResponse<Void>> toggleDislike(
            @PathVariable("postId") Long postId,
            @RequestParam("userId") Long userId) {
        ApiResponse<Void> response = postService.toggleDislike(postId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/nearby/recent")
    public ResponseEntity<ApiResponse<NearbyPostResponseDto>> getRecentNearbyPosts(
            @RequestBody NearbyPostRequestDto nearbyPostRequestDto,
            @RequestParam("page") int page,
            @RequestParam("excludeCompleted") boolean excludeCompleted) {
        return ResponseEntity.ok(postService.getRecentNearbyPosts(nearbyPostRequestDto, page, excludeCompleted));
    }

    @GetMapping("/posts/nearby/top")
    public ResponseEntity<ApiResponse<NearbyPostResponseDto>> getTopNearbyPosts(
            @RequestBody NearbyPostRequestDto nearbyPostRequestDto,
            @RequestParam("page") int page,
            @RequestParam("excludeCompleted") boolean excludeCompleted) {
        return ResponseEntity.ok(postService.getTopNearbyPosts(nearbyPostRequestDto, page, excludeCompleted));
    }

    @GetMapping("/posts/nearby/distance")
    public ResponseEntity<ApiResponse<NearbyPostResponseDto>> getDistanceNearbyPosts(
            @RequestBody NearbyPostRequestDto nearbyPostRequestDto,
            @RequestParam("page") int page,
            @RequestParam("excludeCompleted") boolean excludeCompleted) {
        return ResponseEntity.ok(postService.getDistanceNearbyPosts(nearbyPostRequestDto, page, excludeCompleted));
    }
}
