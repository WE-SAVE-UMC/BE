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
import com.example.we_save.domain.user.service.UserService;
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
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostImageService postImageService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @RequestPart("postRequestDto") PostRequestDto postRequestDto,
            @RequestPart("images") List<MultipartFile> files) {

        Post savePost = postService.createPost(postRequestDto); //게시글 정보 등록

        try {
            postImageService.savePostImages(files, savePost); //게시글 사진 등록
            return ResponseEntity.ok(ApiResponse.onPostSuccess(PostResponseDto.builder().postId(savePost.getId()).build(), SuccessStatus._POST_OK));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400", e.getMessage(), null));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400","파일 업로드 오류",null));
        }

    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost(
            @PathVariable("postId") Long postId,
            @RequestPart("postRequestDto") PostRequestDto postRequestDto,
            @RequestPart("images") List<MultipartFile> files) {
        Post updatePost = postService.updatePost(postId, postRequestDto);//게시글 정보 수정, 게시글 이미지 DB정보 삭제

        try {
            postImageService.deletePostAllImage(postId); //서버에 있는 기존 게시글 이미지 삭제
            postImageService.savePostImages(files, updatePost); //서버에 새로 이미지 등록
            return ResponseEntity.ok(ApiResponse.onGetSuccess(PostResponseDto.builder().postId(updatePost.getId()).build()));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400", e.getMessage(), null));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400","파일 업로드 오류",null));
        }
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

    @GetMapping("/posts/search/nearby")
    public ResponseEntity<ApiResponse<NearbyPostResponseDto>> searchNearbyPosts(
            @RequestParam("query") String query,
            @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy,
            @RequestParam("page") int page,
            @RequestParam("regionName") String regionName,
            @RequestParam("longitude") double longitude,
            @RequestParam("latitude") double latitude,
            @RequestParam(value = "excludeCompleted", defaultValue = "false") boolean excludeCompleted) {

        NearbyPostRequestDto nearbyPostRequestDto = NearbyPostRequestDto.builder()
                .regionName(regionName)
                .longitude(longitude)
                .latitude(latitude)
                .build();

        return ResponseEntity.ok(postService.searchNearbyPosts(query, sortBy, nearbyPostRequestDto, page, excludeCompleted));
    }

    @GetMapping("/posts/search/domestic")
    public ResponseEntity<ApiResponse<List<DomesticPostDto>>> searchDomesticPosts(
            @RequestParam("query") String query,
            @RequestParam(value = "sortBy", defaultValue = "recent") String sortBy,
            @RequestParam("page") int page,
            @RequestParam(value = "excludeCompleted", defaultValue = "false") boolean excludeCompleted) {

        return ResponseEntity.ok(postService.searchDomesticPosts(query, sortBy, page, excludeCompleted));
    }

}
