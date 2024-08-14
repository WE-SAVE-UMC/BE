package com.example.we_save.domain.comment.application;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.ErrorStatus;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.comment.controller.request.CommentRequestDto;
import com.example.we_save.domain.comment.controller.response.CommentResponseDto;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.comment.entity.CommentImage;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentReportRepository commentReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentImageRepository commentImageRepository;

    @Autowired
    private RegionUtil regionUtil;

    private static final int MAX_IMAGE_COUNT = 10;
    private static final int MAX_REPORT_COUNT = 10;

    @Override
    @Transactional
    public ApiResponse<CommentResponseDto> createComment(CommentRequestDto commentRequestDto) {

        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(() ->
                new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (commentRequestDto.getImages().size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }

        User user = User.builder().id(commentRequestDto.getUserId()).build();
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(commentRequestDto.getContent())
                .build();
        List<CommentImage> commentImages = commentRequestDto.getImages().stream()
                .filter(imageUrl -> imageUrl != null && !imageUrl.trim().isEmpty())
                .map(imageUrl -> CommentImage.builder().imageUrl(imageUrl).comment(comment).build())
                .collect(Collectors.toList());

        comment.setImages(commentImages);

        Comment savedComment = commentRepository.save(comment);

        post.setComments(post.getComments() + 1);
        postRepository.save(post);

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setCommentId(savedComment.getId());

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(commentRequestDto.getUserId()) // <- 이 부분
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다.")); // <- 이 부분

        if (!comment.getPost().equals(post)) {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        if (!comment.getUser().equals(user)) {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        if (commentRequestDto.getImages().size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }
        // 기존 이미지 가져오기
        List<String> existingImageUrls = comment.getImages().stream()
                .map(CommentImage::getImageUrl)
                .collect(Collectors.toList());

        // 빈 문자열로 된 이미지를 제거할 이미지로 간주
        List<String> imagesToDelete = existingImageUrls.stream()
                .filter(url -> commentRequestDto.getImages().contains("") || !commentRequestDto.getImages().contains(url))
                .collect(Collectors.toList());

        // 삭제할 이미지 DB에서 삭제
        imagesToDelete.forEach(url -> {
            CommentImage imageToDelete = comment.getImages().stream()
                    .filter(image -> image.getImageUrl().equals(url))
                    .findFirst()
                    .orElse(null);
            if (imageToDelete != null) {
                commentImageRepository.delete(imageToDelete);
            }
        });

        // 댓글 내용 및 이미지 업데이트
        comment.setContent(commentRequestDto.getContent());

        List<CommentImage> updatedImages = commentRequestDto.getImages().stream()
                .filter(imageUrl -> !imageUrl.isEmpty())  // 빈 문자열 이미지는 추가하지 않음
                .map(imageUrl -> CommentImage.builder().imageUrl(imageUrl).comment(comment).build())
                .collect(Collectors.toList());

        comment.getImages().clear();
        comment.getImages().addAll(updatedImages);

        Comment updatedComment = commentRepository.save(comment);

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setCommentId(updatedComment.getId());

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }


    @Override
    @Transactional
    public ApiResponse<CommentResponseDto> deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));


        Post post = comment.getPost();


        commentRepository.delete(comment);


        post.setComments(post.getComments() - 1);
        postRepository.save(post);


        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setCommentId(commentId);

        return ApiResponse.onDeleteSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<Void> reportComment(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (commentReportRepository.existsByCommentIdAndUserId(commentId, userId)) {
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