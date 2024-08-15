package com.example.we_save.domain.comment.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.domain.comment.controller.request.CommentRequestDto;
import com.example.we_save.domain.comment.controller.response.CommentResponseDto;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.user.entity.User;

public interface CommentService {
    Comment createComment(CommentRequestDto commentRequestDto, User user);
    Comment updateComment(Long commentId, CommentRequestDto commentRequestDto, User user);
    ApiResponse<CommentResponseDto> deleteComment(Long commentId);
    ApiResponse<Void> reportComment(Long postId, User user);
}
