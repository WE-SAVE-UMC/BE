package com.example.we_save.domain.user.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.user.controller.request.BlockRequestDto;
import com.example.we_save.domain.user.controller.response.BlockResponseDto;
import com.example.we_save.domain.user.controller.response.UserPostResponseDto;
import com.example.we_save.domain.user.converter.BlockConverter;
import com.example.we_save.domain.user.entity.Block;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.service.UserAuthCommandService;
import com.example.we_save.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserAuthCommandService userAuthCommandService;

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

    @Operation(summary = "차단 하기",  security = @SecurityRequirement(name="Authorization"))
    @PostMapping("/block")
    public ResponseEntity<ApiResponse> addBlockUser(@RequestBody @Valid BlockRequestDto.BlockDto request) {
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        try {
            Block block= userService.addBlock(user,request.getTargetId());

            if (block == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400", "400 Bad Request, 자기자신을 차단할 수 없습니다..", block));
            } else {
                ApiResponse<BlockResponseDto.BlockResultDto> response = ApiResponse.onPostSuccess(BlockConverter.toBlockResultDto(block), SuccessStatus._POST_OK);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        }catch (IllegalArgumentException e){
            ApiResponse<BlockResponseDto.BlockResultDto> errorResponse = ApiResponse.onFailure(
                    "COMMON400", e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "차단 해제", security = @SecurityRequirement(name="Authorization"))
    @DeleteMapping("/block/{targetId}")
    public ResponseEntity<ApiResponse> deleteBlockUser(@PathVariable("targetId") long targetId) {
        User user = userAuthCommandService.getAuthenticatedUserInfo();
        try{
            userService.deleteBlock(user,targetId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }catch (IllegalArgumentException e){
            ApiResponse<BlockResponseDto.BlockResultDto> errorResponse = ApiResponse.onFailure(
                    "COMMON400", e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }



}
