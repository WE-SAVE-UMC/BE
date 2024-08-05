package com.example.we_save.domain.comment.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.comment.controller.request.CommentRequestDto;
import com.example.we_save.domain.comment.controller.response.CommentResponseDto;

public interface CommentService {
    ApiResponse<CommentResponseDto> createComment(CommentRequestDto commentRequestDto);
    ApiResponse<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto);
    ApiResponse<CommentResponseDto> deleteComment(Long commentId);
    ApiResponse<Void> reportComment(Long postId, Long userId);
}
