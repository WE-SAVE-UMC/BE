package com.example.we_save.domain.comment.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.comment.application.CommentImageService;
import com.example.we_save.domain.comment.application.CommentService;
import com.example.we_save.domain.comment.controller.request.CommentRequestDto;
import com.example.we_save.domain.comment.controller.response.CommentResponseDto;
import com.example.we_save.domain.comment.entity.Comment;
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
@RequestMapping("/api/posts/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentImageService commentImageService;

    @Autowired
    private UserAuthCommandService userAuthCommandService;

    @Operation(summary = "댓글 작성", security = @SecurityRequirement(name="Authorization"))
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @RequestPart("commentRequestDto") CommentRequestDto commentRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        User user = userAuthCommandService.getAuthenticatedUserInfo();

        // 댓글 생성
        Comment saveComment = commentService.createComment(commentRequestDto, user);

        try {
            // 이미지가 존재할 때만 저장 로직 실행
            if (files != null && !files.isEmpty()) {
                commentImageService.saveCommentImage(files, saveComment);
            }
            return ResponseEntity.ok(ApiResponse.onPostSuccess(CommentResponseDto.builder().commentId(saveComment.getId()).build(), SuccessStatus._POST_OK));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.onFailure("COMMON500", "파일 업로드 오류", null));
        }
    }

    @Operation(summary = "댓글 수정", security = @SecurityRequirement(name="Authorization"))
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestPart("commentRequestDto") CommentRequestDto commentRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        User user = userAuthCommandService.getAuthenticatedUserInfo();
        // 댓글 수정
        Comment updatedComment = commentService.updateComment(commentId, commentRequestDto,user);

        try {
            // 기존 이미지 삭제
            commentImageService.deleteCommentAllImage(commentId);

            // 새로운 이미지가 있을 경우에만 저장
            if (files != null && !files.isEmpty()) {
                commentImageService.saveCommentImage(files, updatedComment);
            }

            return ResponseEntity.ok(ApiResponse.onGetSuccess(CommentResponseDto.builder().commentId(updatedComment.getId()).build()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("COMMON400", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.onFailure("COMMON500", "파일 업로드 오류", null));
        }
    }
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> deleteComment(
            @PathVariable("commentId") Long commentId) {

        ApiResponse<CommentResponseDto> responseDto = commentService.deleteComment(commentId);
        try {
            // 이미지 삭제
            commentImageService.deleteCommentAllImage(commentId);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.onFailure("COMMON500", "파일 삭제 오류", null));
        }
    }

    @Operation(summary = "댓글 신고", security = @SecurityRequirement(name="Authorization"))
    @PostMapping("/{commentId}/report")
    public ResponseEntity<ApiResponse<Void>> reportComment(
            @PathVariable("commentId") Long commentId) {

        User user = userAuthCommandService.getAuthenticatedUserInfo();
        ApiResponse<Void> response = commentService.reportComment(commentId, user);
        return ResponseEntity.ok(response);
    }
}
