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
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.service.UserAuthCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Autowired
    private UserAuthCommandService userAuthCommandService;

    @Operation(summary = "게시글 작성", security = @SecurityRequirement(name = "Authorization"))
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @RequestPart("postRequestDto") PostRequestDto postRequestDto,
            @RequestPart("images") List<MultipartFile> files) {

        User user = userAuthCommandService.getAuthenticatedUserInfo();

        Post savePost = postService.createPost(postRequestDto, user); //게시글 정보 등록

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

    @Operation(summary = "게시글 수정", security = @SecurityRequirement(name="Authorization"))
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost(
            @PathVariable("postId") Long postId,
            @RequestPart("postRequestDto") PostRequestDto postRequestDto,
            @RequestPart("images") List<MultipartFile> files) {

        User user = userAuthCommandService.getAuthenticatedUserInfo();
        Post updatePost = postService.updatePost(postId, postRequestDto,user);//게시글 정보 수정, 게시글 이미지 DB정보 삭제

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

    @Operation(summary = "게시글 보기", security = @SecurityRequirement(name="Authorization"))
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDtoWithComments>> getPost(
            @PathVariable("postId") Long postId){

        User user = userAuthCommandService.getAuthenticatedUserInfo();
        ApiResponse<PostResponseDtoWithComments> responseDto = postService.getPost(postId,user);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "게시글 신고", security = @SecurityRequirement(name="Authorization"))
    @PostMapping("/posts/{postId}/report")
    public ResponseEntity<ApiResponse<Void>> reportPost(
            @PathVariable("postId") Long postId) {
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        ApiResponse<Void> response = postService.reportPost(postId, user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 확인했어요", security = @SecurityRequirement(name="Authorization"))
    @PostMapping("/posts/{postId}/heart")
    public ResponseEntity<ApiResponse<Void>> toggleHeart(
            @PathVariable("postId") Long postId) {
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        ApiResponse<Void> response = postService.toggleHeart(postId, user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 허위에요", security = @SecurityRequirement(name="Authorization"))
    @PostMapping("/posts/{postId}/dislike")
    public ResponseEntity<ApiResponse<Void>> toggleDislike(
            @PathVariable("postId") Long postId) {
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        ApiResponse<Void> response = postService.toggleDislike(postId, user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "근처 게시글 최근순")
    @PostMapping("/posts/nearby/recent")
    public ResponseEntity<ApiResponse<NearbyPostResponseDto>> getRecentNearbyPosts(
            @RequestBody NearbyPostRequestDto nearbyPostRequestDto,
            @RequestParam("page") int page,
            @RequestParam("excludeCompleted") boolean excludeCompleted) {
        return ResponseEntity.ok(postService.getRecentNearbyPosts(nearbyPostRequestDto, page, excludeCompleted));
    }

    @Operation(summary = "근처 게시글 확인순")
    @PostMapping("/posts/nearby/top")
    public ResponseEntity<ApiResponse<NearbyPostResponseDto>> getTopNearbyPosts(
            @RequestBody NearbyPostRequestDto nearbyPostRequestDto,
            @RequestParam("page") int page,
            @RequestParam("excludeCompleted") boolean excludeCompleted) {
        return ResponseEntity.ok(postService.getTopNearbyPosts(nearbyPostRequestDto, page, excludeCompleted));
    }

    @Operation(summary = "근처 게시글 거리순")
    @PostMapping("/posts/nearby/distance")
    public ResponseEntity<ApiResponse<NearbyPostResponseDto>> getDistanceNearbyPosts(
            @RequestBody NearbyPostRequestDto nearbyPostRequestDto,
            @RequestParam("page") int page,
            @RequestParam("excludeCompleted") boolean excludeCompleted) {
        return ResponseEntity.ok(postService.getDistanceNearbyPosts(nearbyPostRequestDto, page, excludeCompleted));
    }

    @Operation(summary = "국내 게시글 최신순")
    @PostMapping("/posts/domestic/recent")
    public ResponseEntity<ApiResponse<List<DomesticPostDto>>> getRecentDomesticPosts(
            @RequestParam(value = "excludeCompleted", required = false, defaultValue = "false") boolean excludeCompleted) {
        ApiResponse<List<DomesticPostDto>> response = postService.getRecentDomesticPosts(excludeCompleted);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "국내 게시글 확인순")
    @PostMapping("/posts/domestic/top")
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
