package com.example.we_save.domain.comment.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.ErrorStatus;
import com.example.we_save.domain.comment.controller.request.CommentRequestDto;
import com.example.we_save.domain.comment.controller.response.CommentResponseDto;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.comment.entity.CommentReport;
import com.example.we_save.domain.comment.repository.CommentImageRepository;
import com.example.we_save.domain.comment.repository.CommentReportRepository;
import com.example.we_save.domain.comment.repository.CommentRepository;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.repository.PostRepository;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentReportRepository commentReportRepository;

    @Autowired
    private CommentImageRepository commentImageRepository;

    @Autowired
    private CommentImageService commentImageService;

    private static final int MAX_REPORT_COUNT = 10;

    @Override
    @Transactional
    public Comment createComment(CommentRequestDto commentRequestDto, User user) {
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(() ->
                new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(commentRequestDto.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        post.setComments(post.getComments() + 1);
        postRepository.save(post);

        return savedComment;
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        comment.setContent(commentRequestDto.getContent());

        commentImageRepository.deleteByCommentId(commentId); //기존 댓글 이미지 삭제

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public ApiResponse<CommentResponseDto> deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        Post post = comment.getPost();

        try {
            commentImageService.deleteCommentAllImage(commentId); // 댓글 이미지 삭제
        } catch (Exception e) {
            return ApiResponse.onFailure("COMMON400", "파일 삭제 오류", null);
        }

        commentRepository.delete(comment);

        post.setComments(post.getComments() - 1);
        postRepository.save(post);

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setCommentId(commentId);

        return ApiResponse.onDeleteSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<Void> reportComment(Long commentId, User user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (commentReportRepository.existsByCommentIdAndUserId(commentId, user.getId())) {
            return ApiResponse.onFailure(ErrorStatus._ALREADY_REPORTED.getCode(), ErrorStatus._ALREADY_REPORTED.getMessage(), null);
        }

        CommentReport report = CommentReport.builder()
                .comment(comment)
                .user(user)
                .build();
        commentReportRepository.save(report);

        int reportCount = commentReportRepository.countByCommentId(commentId);

        if (reportCount >= MAX_REPORT_COUNT) {

            commentReportRepository.deleteByCommentId(commentId);

            commentImageRepository.deleteByCommentId(commentId);

            commentRepository.delete(comment);

            Post post = comment.getPost();
            post.setComments(post.getComments() - 1);
            postRepository.save(post);

            return ApiResponse.onReportSuccess(null);
        }

        return ApiResponse.onReportSuccess(null);
    }
}