package com.example.we_save.domain.post.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.post.applicaiton.PostImageService;
import com.example.we_save.domain.post.applicaiton.PostService;
import com.example.we_save.domain.post.controller.request.NearbyPostRequestDto;
import com.example.we_save.domain.post.controller.request.PostRequestDto;
import com.example.we_save.domain.post.controller.response.DomesticPostDto;
import com.example.we_save.domain.post.controller.response.NearbyPostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDtoWithComments;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.entity.PostImage;
import com.example.we_save.image.entity.Image;
import com.example.we_save.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostImageService postImageService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @RequestPart PostRequestDto postRequestDto,
            @RequestPart("images") List<MultipartFile> files) {

        Post savePost = postService.createPost(postRequestDto);

        try {
            postImageService.savePostImages(files, savePost);
            return ResponseEntity.ok(ApiResponse.onPostSuccess(PostResponseDto.builder().postId(savePost.getId()).build(), SuccessStatus._POST_OK));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400","파일 업로드 오류",null));
        }

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
        try {
            postImageService.deletePostAllImage(postId);
            return ResponseEntity.ok(responseDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400","파일 업로드 오류",null));
        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDtoWithComments>> getPost(
            @PathVariable("postId") Long postId,
            @RequestParam("userId") Long userId){
        ApiResponse<PostResponseDtoWithComments> responseDto = postService.getPost(postId,userId);
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

    @GetMapping("/posts/domestic/recent")
    public ResponseEntity<ApiResponse<List<DomesticPostDto>>> getRecentDomesticPosts(
            @RequestParam(value = "excludeCompleted", required = false, defaultValue = "false") boolean excludeCompleted) {
        ApiResponse<List<DomesticPostDto>> response = postService.getRecentDomesticPosts(excludeCompleted);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/domestic/top")
    public ResponseEntity<ApiResponse<List<DomesticPostDto>>> getTopDomesticPosts(
            @RequestParam(value = "excludeCompleted", required = false, defaultValue = "false") boolean excludeCompleted) {
        ApiResponse<List<DomesticPostDto>> response = postService.getTopDomesticPosts(excludeCompleted);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시물 상황종료 처리")
    @PutMapping("/posts/status/{postId}")
    public ResponseEntity<ApiResponse<Void>> changeToPostCompleted(@PathVariable("postId") long postId) {

        return ResponseEntity.ok(postService.changeToPostCompleted(postId));
    }
}
