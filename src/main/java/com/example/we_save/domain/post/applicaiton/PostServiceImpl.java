package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.ErrorStatus;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.comment.controller.response.CommentDto;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.comment.entity.CommentImage;
import com.example.we_save.domain.comment.repository.CommentRepository;
import com.example.we_save.domain.post.controller.request.PostRequestDto;
import com.example.we_save.domain.post.controller.response.PostResponseDto;
import com.example.we_save.domain.post.controller.response.PostResponseDtoWithComments;
import com.example.we_save.domain.post.entity.*;
import com.example.we_save.domain.post.repository.PostDislikeRepository;
import com.example.we_save.domain.post.repository.PostHeartRepository;
import com.example.we_save.domain.post.repository.PostReportRepository;
import com.example.we_save.domain.post.repository.PostRepository;
import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import com.example.we_save.domain.region.repository.EupmyeondongRepository;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    private UserRepository userRepository;

    private static final int MAX_IMAGE_COUNT = 10;
    private static final int MAX_REPORT_COUNT = 10;

    @Override
    @Transactional
    public ApiResponse<PostResponseDto> createPost(PostRequestDto postRequestDto) {
        if (postRequestDto.getImages().size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }
        User user = userRepository.findById(postRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));


        Post post = Post.builder()
                .user(user)
                .category(postRequestDto.getCategory())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .status(PostStatus.PROCESSING)
                .longitude(postRequestDto.getLongitude())
                .latitude(postRequestDto.getLatitude())
                .postRegionName(postRequestDto.getPostRegionName())
                .hearts(0)
                .dislikes(0)
                .comments(0)
                .report119(postRequestDto.isReport119())
                .reportCount(0)
                .build();

        List<PostImage> postImages = postRequestDto.getImages().stream()
                .map(imageUrl -> PostImage.builder().imageUrl(imageUrl).post(post).build())
                .collect(Collectors.toList());
        post.setImages(postImages);

        Post savedPost = postRepository.save(post);

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setPost(savedPost);

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<PostResponseDto> updatePost(Long postId, PostRequestDto postRequestDto) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        if (postRequestDto.getImages().size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }
        Post post = optionalPost.get();

        post.setCategory(postRequestDto.getCategory());
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setStatus(PostStatus.PROCESSING);
        post.setReport119(postRequestDto.isReport119());

        post.getImages().clear();
        List<PostImage> postImages = postRequestDto.getImages().stream()
                .map(imageUrl -> PostImage.builder().imageUrl(imageUrl).post(post).build())
                .collect(Collectors.toList());
        post.setImages(postImages);

        Post updatedPost = postRepository.save(post);

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setPost(updatedPost);

        return ApiResponse.onPostSuccess(responseDto, SuccessStatus._POST_OK);

    }

    @Override
    @Transactional
    public ApiResponse<PostResponseDto> deletePost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        Post postToDelete = optionalPost.get();

        commentRepository.deleteByPostId(postId);

        postRepository.delete(postToDelete);

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setPost(postToDelete);

        return ApiResponse.onDeleteSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<PostResponseDtoWithComments> getPost(Long postId){
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        Post post = optionalPost.get();

        // 모든 댓글 가져오기
        List<Comment> comments = commentRepository.findByPostId(postId);
        //댓글을 commentdto 로 변환
        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setUserId(comment.getUser().getId());
            dto.setContent(comment.getContent());
            List<String> imageUrls = comment.getImages().stream()
                    .map(CommentImage::getImageUrl)
                    .collect(Collectors.toList());
            dto.setImages(imageUrls);
            dto.setCreatedAt(comment.getCreateAt());
            dto.setUpdatedAt(comment.getUpdateAt());
            return dto;
        }).collect(Collectors.toList());

        PostResponseDtoWithComments responseDto = PostResponseDtoWithComments.builder()
                .id(post.getId())
                .user(post.getUser())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .longitude(post.getLongitude())
                .latitude(post.getLatitude())
                .postRegionName(post.getPostRegionName())
                .hearts(post.getHearts())
                .dislikes(post.getDislikes())
                .comments(comments.size())
                .createdAt(post.getCreateAt())
                .updatedAt(post.getUpdateAt())
                .images(post.getImages().stream()
                        .map(PostImage::getImageUrl)
                        .collect(Collectors.toList()))
                .commentsList(commentDtos)
                .build();

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
            return ApiResponse.onReportSuccess(null);
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
