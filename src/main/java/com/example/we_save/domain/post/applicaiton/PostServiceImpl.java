package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.ErrorStatus;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.comment.controller.response.CommentDto;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.comment.repository.CommentRepository;
import com.example.we_save.domain.post.controller.request.PostRequestDto;
import com.example.we_save.domain.post.controller.response.PostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDtoWithComments;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.entity.PostDislike;
import com.example.we_save.domain.post.entity.PostHeart;
import com.example.we_save.domain.post.entity.PostReport;
import com.example.we_save.domain.post.repository.PostDislikeRepository;
import com.example.we_save.domain.post.repository.PostHeartRepository;
import com.example.we_save.domain.post.entity.PostStatus;
import com.example.we_save.domain.post.repository.PostReportRepository;
import com.example.we_save.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private PostHeartRepository postHeartRepository;

    @Autowired
    private PostDislikeRepository postDislikeRepository;

    private static final int MAX_IMAGE_COUNT = 10;
    private static final int MAX_REPORT_COUNT = 10;

    @Override
    public ApiResponse<PostResponseDto> createPost(PostRequestDto postRequestDto) {
        if (postRequestDto.getImages().size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }

        Post post = new Post();
        post.setUserId(postRequestDto.getUserId());
        post.setCategoryId(postRequestDto.getCategoryId());
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setLongitude(postRequestDto.getLongitude());
        post.setLatitude(postRequestDto.getLatitude());
        post.setStatus(PostStatus.PROCESSING);
        post.setPostRegionName(postRequestDto.getPostRegionName());
        post.setImages(postRequestDto.getImages());
        post.setReport119(postRequestDto.isReport119());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setPostId(savedPost.getId());

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }

    @Override
    public ApiResponse<PostResponseDto> updatePost(Long postId, PostRequestDto postRequestDto) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        if (postRequestDto.getImages().size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }

        Post post = optionalPost.get();

        if (!post.getUserId().equals(postRequestDto.getUserId())) {
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), ErrorStatus._BAD_REQUEST.getMessage(), null);
        }

        post.setUserId(postRequestDto.getUserId());
        post.setCategoryId(postRequestDto.getCategoryId());
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setStatus(PostStatus.COMPLETED);
        post.setLongitude(postRequestDto.getLongitude());
        post.setLatitude(postRequestDto.getLatitude());
        post.setPostRegionName(postRequestDto.getPostRegionName());
        post.setImages(postRequestDto.getImages());
        post.setReport119(postRequestDto.isReport119());
        post.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setPostId(updatedPost.getId());

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<PostResponseDto> deletePost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        commentRepository.deleteByPostId(postId);

        postRepository.delete(optionalPost.get());

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setPostId(postId);

        return ApiResponse.onDeleteSuccess(responseDto);
    }

    @Override
    public ApiResponse<PostResponseDtoWithComments> getPost(Long postId){
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        Post post = optionalPost.get();

        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setUserId(comment.getUserId());
            dto.setContent(comment.getContent());
            dto.setImages(comment.getImages());
            dto.setCreatedAt(comment.getCreatedAt());
            dto.setUpdatedAt(comment.getUpdatedAt());
            return dto;
        }).collect(Collectors.toList());

        PostResponseDtoWithComments responseDto = new PostResponseDtoWithComments();
        responseDto.setId(post.getId());
        responseDto.setUserId(post.getUserId());
        responseDto.setCategoryId(post.getCategoryId());
        responseDto.setTitle(post.getTitle());
        responseDto.setContent(post.getContent());
        responseDto.setStatus(post.getStatus());
        responseDto.setLongitude(post.getLongitude());
        responseDto.setLatitude(post.getLatitude());
        responseDto.setPostRegionName(post.getPostRegionName());
        responseDto.setHearts(post.getHearts());
        responseDto.setDislikes(post.getDislikes());
        responseDto.setComments(comments.size());
        responseDto.setCreatedAt(post.getCreatedAt());
        responseDto.setUpdatedAt(post.getUpdatedAt());
        responseDto.setImages(post.getImages());
        responseDto.setCommentsList(commentDtos);

        return ApiResponse.onGetSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<Void> reportPost(Long postId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure( ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        if (postReportRepository.existsByPostIdAndUserId(postId, userId)) {
            return ApiResponse.onFailure(ErrorStatus._ALREADY_REPORTED.getCode(), ErrorStatus._ALREADY_REPORTED.getMessage(), null);
        }

        PostReport report = new PostReport();
        report.setPostId(postId);
        report.setUserId(userId);
        postReportRepository.save(report);

        int reportCount = postReportRepository.countByPostId(postId);

        if (reportCount >= MAX_REPORT_COUNT) {
            commentRepository.deleteByPostId(postId);
            postRepository.deleteById(postId);
            return ApiResponse.onDeleteSuccess(null);
        }

        return ApiResponse.onReportSuccess(null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> toggleHeart(Long postId, Long userId){
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(),ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        Post post = optionalPost.get();

        // 이미 허위에요 를 누른 경우
        if(postDislikeRepository.existsByPostIdAndUserId(postId, userId)){
            return ApiResponse.onFailure(ErrorStatus._ALREADY_REPORTED.getCode(), "이미 '허위에요'를 누르셨습니다.", null );
        }

        //이미 확인했어요 를 누른 경우 취소, 확인했어요 등록
        if(postHeartRepository.existsByPostIdAndUserId(postId, userId)){
            postHeartRepository.deleteByPostIdAndUserId(postId, userId);
            post.setHearts(post.getHearts() - 1);
            postRepository.save(post);
            return ApiResponse.onCancelSuccess(null);
        } else {
            PostHeart heart = new PostHeart();
            heart.setPostId(postId);
            heart.setUserId(userId);
            postHeartRepository.save(heart);
            post.setHearts(post.getHearts() + 1);
            postRepository.save(post);

            return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
        }
    }

    @Override
    @Transactional
    public ApiResponse<Void> toggleDislike(Long postId, Long userId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }
        Post post = optionalPost.get();

        // 이미 확인했어요 를 누른 경우
        if (postHeartRepository.existsByPostIdAndUserId(postId, userId)) {
            return ApiResponse.onFailure(ErrorStatus._ALREADY_REPORTED.getCode(), "이미 '확인했어요'를 누르셨습니다.", null);
        }

        // 이미 허위에요 를 누른 경우 취소, 허위에요 등록
        if (postDislikeRepository.existsByPostIdAndUserId(postId, userId)) {
            postDislikeRepository.deleteByPostIdAndUserId(postId, userId);
            post.setDislikes(post.getDislikes() - 1);
            postRepository.save(post);

            return ApiResponse.onCancelSuccess(null);
        } else {
            PostDislike dislike = new PostDislike();
            dislike.setPostId(postId);
            dislike.setUserId(userId);
            postDislikeRepository.save(dislike);
            post.setDislikes(post.getDislikes() + 1);
            postRepository.save(post);

            return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);

        }
    }
}
