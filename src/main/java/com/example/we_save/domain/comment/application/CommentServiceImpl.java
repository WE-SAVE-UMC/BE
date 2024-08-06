package com.example.we_save.domain.comment.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.ErrorStatus;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.comment.controller.request.CommentRequestDto;
import com.example.we_save.domain.comment.controller.response.CommentResponseDto;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.comment.entity.CommentReport;
import com.example.we_save.domain.comment.repository.CommentReportRepository;
import com.example.we_save.domain.comment.repository.CommentRepository;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    private static final int MAX_IMAGE_COUNT = 10;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentReportRepository commentReportRepository;

    private static final int MAX_REPORT_COUNT = 10;

    @Override
    @Transactional
    public ApiResponse<CommentResponseDto> createComment(CommentRequestDto commentRequestDto) {
        if (!postRepository.existsById(commentRequestDto.getPostId())) {
            // 실패 응답 생성
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        if (commentRequestDto.getImages().size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }

        Comment comment = new Comment();
        comment.setPostId(commentRequestDto.getPostId());
        comment.setUserId(commentRequestDto.getUserId());
        comment.setContent(commentRequestDto.getContent());
        comment.setImages(commentRequestDto.getImages());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(() ->
                new EntityNotFoundException("Post not found"));
        post.setComments(post.getComments() + 1);
        postRepository.save(post);

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setCommentId(savedComment.getId());

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (!optionalComment.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        if (commentRequestDto.getImages().size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }


        Comment comment = optionalComment.get();

        if (!comment.getPostId().equals(commentRequestDto.getPostId())) {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        if (!comment.getUserId().equals(commentRequestDto.getUserId())) {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        comment.setContent(commentRequestDto.getContent());
        comment.setImages(commentRequestDto.getImages());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(comment);

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setCommentId(updatedComment.getId());

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<CommentResponseDto> deleteComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (!optionalComment.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        Comment comment = optionalComment.get();
        commentRepository.delete(optionalComment.get());

        Post post = postRepository.findById(comment.getPostId()).orElseThrow(() ->
                new EntityNotFoundException("Post not found"));
        post.setComments(post.getComments() - 1);
        postRepository.save(post);

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setCommentId(commentId);

        return ApiResponse.onDeleteSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<Void> reportComment(Long commentId, Long userId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (!optionalComment.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        if (commentReportRepository.existsByCommentIdAndUserId(commentId, userId)) {
            return ApiResponse.onFailure(ErrorStatus._ALREADY_REPORTED.getCode(), ErrorStatus._ALREADY_REPORTED.getMessage(), null);
        }

        CommentReport report = new CommentReport();
        report.setCommentId(commentId);
        report.setUserId(userId);
        commentReportRepository.save(report);

        int reportCount = commentReportRepository.countByCommentId(commentId);

        if (reportCount >= MAX_REPORT_COUNT) {
            commentRepository.deleteById(commentId);

            Post post = postRepository.findById(optionalComment.get().getPostId()).orElseThrow(() ->
                    new EntityNotFoundException("Post not found"));
            post.setComments(post.getComments() - 1);
            postRepository.save(post);

            return ApiResponse.onReportSuccess(null);
        }

        return ApiResponse.onReportSuccess(null);
    }
}