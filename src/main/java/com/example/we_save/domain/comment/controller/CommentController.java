package com.example.we_save.domain.comment.controller;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.comment.application.CommentService;
import com.example.we_save.domain.comment.controller.request.CommentRequestDto;
import com.example.we_save.domain.comment.controller.response.CommentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @RequestBody CommentRequestDto commentRequestDto) {
        ApiResponse<CommentResponseDto> responseDto = commentService.createComment(commentRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDto commentRequestDto) {
        ApiResponse<CommentResponseDto> responseDto = commentService.updateComment(commentId, commentRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> deleteComment(
            @PathVariable("commentId") Long commentId) {
        ApiResponse<CommentResponseDto> responseDto = commentService.deleteComment(commentId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/{commentId}/report")
    public ResponseEntity<ApiResponse<Void>> reportComment(
            @PathVariable("commentId") Long commentId,
            @RequestParam("userId") Long userId) {
        ApiResponse<Void> response = commentService.reportComment(commentId, userId);
        return ResponseEntity.ok(response);
    }
}
