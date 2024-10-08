package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.ErrorStatus;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.comment.controller.response.CommentDto;
import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.comment.entity.CommentImage;
import com.example.we_save.domain.comment.repository.CommentRepository;
import com.example.we_save.domain.notification.application.NotificationService;
import com.example.we_save.domain.notification.controller.request.NotificationRequestDto;
import com.example.we_save.domain.post.controller.request.NearbyPostRequestDto;
import com.example.we_save.domain.post.controller.request.PostRequestDto;
import com.example.we_save.domain.post.controller.response.*;
import com.example.we_save.domain.post.entity.*;
import com.example.we_save.domain.post.repository.*;
import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import com.example.we_save.domain.region.repository.EupmyeondongRepository;
import com.example.we_save.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private PostImageRepository postImageRepository;

    @Autowired
    private RegionUtil regionUtil;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EupmyeondongRepository eupmyeondongRepository;

    private static final int MAX_REPORT_COUNT = 10;

    private final int PAGE_SIZE = 10;
    private final int RECENT = 0;
    private final int TOP = 1;
    private final int DISTANCE = 2;

    @Override
    @Transactional
    public Post createPost(PostRequestDto postRequestDto, User user) {

        if (postRequestDto.getCategory() == null) {
            throw new IllegalArgumentException("카테고리는 필수 입력 사항입니다.");
        }

        long regionId = regionUtil.convertRegionNameToRegionId(postRequestDto.getPostRegionName());
        EupmyeondongRegion region = eupmyeondongRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));


        Post post = Post.builder()
                .user(user)
                .category(postRequestDto.getCategory())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .status(PostStatus.PROCESSING)
                .longitude(postRequestDto.getLongitude())
                .latitude(postRequestDto.getLatitude())
                .postRegionName(postRequestDto.getPostRegionName())
                .region(region)
                .hearts(0)
                .dislikes(0)
                .comments(0)
                .report119(postRequestDto.isReport119())
                .reportCount(0)
                .build();


        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.setCategory(postRequestDto.getCategory());
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setReport119(postRequestDto.isReport119());

        postImageRepository.deleteByPostId(postId); //기존 게시글 이미지 삭제

        return postRepository.save(post);
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

        postHeartRepository.deleteByPostId(postId);

        postDislikeRepository.deleteByPostId(postId);

        postImageRepository.deleteById(postId);

        postRepository.delete(postToDelete);

        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setPostId(postId);

        return ApiResponse.onDeleteSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<PostResponseDtoWithComments> getPost(Long postId, User user){
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (!optionalPost.isPresent()) {
            return ApiResponse.onFailure(ErrorStatus._ARTICLE_NOT_FOUND.getCode(), ErrorStatus._ARTICLE_NOT_FOUND.getMessage(), null);
        }

        Post post = optionalPost.get();

        String postAuthorNickname = post.getUser().getNickname();
        String postAuthorProfileImage = post.getUser().getProfileImage() != null ? post.getUser().getProfileImage().getFilePath() : null;

        //게시글의 사진 가져오기
        List<PostImage> postImageList = postImageRepository.findByPostId(postId);

        List<String> postImageUrls =postImageList.stream()
                .map(PostImage::getFilePath)
                .collect(Collectors.toList());


        // 모든 댓글 가져오기
        List<Comment> comments = commentRepository.findByPostId(postId);

        int totalImageCount = comments.stream()
                .mapToInt(comment -> comment.getImages().size())
                .sum();

        //댓글을 commentdto 로 변환
        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto dto = new CommentDto();
            dto.setId(comment.getId());
            dto.setUserId(comment.getUser().getId());
            dto.setNickname(comment.getUser().getNickname());
            dto.setProfileImage(comment.getUser().getProfileImage() != null ? comment.getUser().getProfileImage().getFilePath() : null);
            dto.setContent(comment.getContent());
            List<String> imageUrls = comment.getImages().stream()
                    .map(CommentImage::getFilePath)
                    .collect(Collectors.toList());
            dto.setImages(imageUrls);
            dto.setCreatedAt(comment.getCreateAt());
            dto.setUpdatedAt(comment.getUpdateAt());
            return dto;
        }).collect(Collectors.toList());

        Boolean userReaction = null;
        if (postHeartRepository.existsByPostIdAndUserId(postId, user.getId())) {
            userReaction = true; // "확인했어요"
        } else if (postDislikeRepository.existsByPostIdAndUserId(postId, user.getId())) {
            userReaction = false; // "허위에요"
        }

        PostResponseDtoWithComments responseDto = PostResponseDtoWithComments.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .nickname(postAuthorNickname)
                .profileImage(postAuthorProfileImage)
                .status(post.getStatus())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .longitude(post.getLongitude())
                .latitude(post.getLatitude())
                .postRegionName(post.getPostRegionName())
                .userReaction(userReaction)
                .hearts(post.getHearts())
                .dislikes(post.getDislikes())
                .comments(comments.size())
                .images(postImageUrls)
                .imageCount(totalImageCount)
                .createdAt(post.getCreateAt())
                .updatedAt(post.getUpdateAt())
                .commentsList(commentDtos)
                .build();

        return ApiResponse.onGetSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<Void> reportPost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 이미 해당 사용자가 해당 게시글에 대해 신고했는지 확인
        if (postReportRepository.existsByPostIdAndUserId(postId, user.getId())) {
            return ApiResponse.onFailure(ErrorStatus._ALREADY_REPORTED.getCode(), ErrorStatus._ALREADY_REPORTED.getMessage(), null);
        }
        PostReport report = PostReport.builder()
                .post(post)
                .user(user)
                .build();

        postReportRepository.save(report);

        int reportCount = postReportRepository.countByPostId(postId);

        if (reportCount >= MAX_REPORT_COUNT) {
            postDislikeRepository.deleteByPostId(postId);
            postHeartRepository.deleteByPostId(postId);
            postReportRepository.deleteByPostId(postId);
            commentRepository.deleteByPostId(postId);
            postRepository.deleteById(postId);
            return ApiResponse.onReportSuccess(null);
        }

        return ApiResponse.onReportSuccess(null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> toggleHeart(Long postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 이미 "허위에요"를 누른 경우
        if (postDislikeRepository.existsByPostIdAndUserId(postId, user.getId())){
            return ApiResponse.onFailure(ErrorStatus._ALREADY_REPORTED.getCode(), "이미 '허위에요'를 누르셨습니다.", null);
        }

        // 이미 "확인했어요"를 누른 경우, 취소
        if (postHeartRepository.existsByPostIdAndUserId(postId, user.getId())) {
            postHeartRepository.deleteByPostIdAndUserId(postId, user.getId());
            post.setHearts(post.getHearts() - 1);
            postRepository.save(post);
            return ApiResponse.onCancelSuccess(null);
        } else {
            PostHeart heart = PostHeart.builder()
                    .post(post)
                    .user(user)
                    .build();
            postHeartRepository.save(heart);

            post.setHearts(post.getHearts() + 1);
            postRepository.save(post);

//            if (post.getHearts() >= 10) {
//                NotificationRequestDto notificationRequest = NotificationRequestDto.builder()
//                        .postId(post.getId())
//                        .title(post.getTitle())
//                        .confirmCount(post.getHearts())
//                        .build();
//
//                notificationService.createPopularNotification(user, notificationRequest);
//            }

            return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
        }
    }

    @Override
    @Transactional
    public ApiResponse<Void> toggleDislike(Long postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        // 이미 "확인했어요"를 누른 경우
        if (postHeartRepository.existsByPostIdAndUserId(postId, user.getId())) {
            return ApiResponse.onFailure(ErrorStatus._ALREADY_REPORTED.getCode(), "이미 '확인했어요'를 누르셨습니다.", null);
        }

        // 이미 "허위에요"를 누른 경우, 취소
        if (postDislikeRepository.existsByPostIdAndUserId(postId, user.getId())) {
            postDislikeRepository.deleteByPostIdAndUserId(postId, user.getId());
            post.setDislikes(post.getDislikes() - 1);
            postRepository.save(post);
            return ApiResponse.onCancelSuccess(null);
        } else {
            PostDislike dislike = PostDislike.builder()
                    .post(post)
                    .user(user)
                    .build();
            postDislikeRepository.save(dislike);

            post.setDislikes(post.getDislikes() + 1);
            postRepository.save(post);

            return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
        }
    }
    @Override
    @Transactional
    public ApiResponse<NearbyPostResponseDto> getRecentNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted) {
        return getNearbyPosts(nearbyPostRequestDto, page, RECENT, excludeCompleted);
    }

    @Override
    @Transactional
    public ApiResponse<NearbyPostResponseDto> getTopNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted) {
        return getNearbyPosts(nearbyPostRequestDto, page, TOP, excludeCompleted);
    }

    @Override
    @Transactional
    public ApiResponse<NearbyPostResponseDto> getDistanceNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted) {
        return getNearbyPosts(nearbyPostRequestDto, page, DISTANCE, excludeCompleted);
    }

    private ApiResponse<NearbyPostResponseDto> getNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, int type, boolean excludeCompleted) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        long regionId = regionUtil.convertRegionNameToRegionId(nearbyPostRequestDto.getRegionName());
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);

        List<Post> posts;

        if (type == RECENT) {
            posts = excludeCompleted
                    ? postRepository.findRecentPostsExcludingCompleted(startDate, regionId, pageable)
                    : postRepository.findRecentPosts(startDate, regionId, pageable);
        } else if (type == TOP) {
            posts = excludeCompleted
                    ? postRepository.findTopNearPostsExcludingCompleted(startDate, regionId, pageable)
                    : postRepository.findTopNearPosts(startDate, regionId, pageable);
        } else if (type == DISTANCE) {
            posts = excludeCompleted
                    ? postRepository.findDistanceNearPostsExcludingCompleted(regionId, nearbyPostRequestDto.getLongitude(), nearbyPostRequestDto.getLatitude(), pageable)
                    : postRepository.findDistanceNearPosts(regionId, nearbyPostRequestDto.getLongitude(), nearbyPostRequestDto.getLatitude(), pageable);
        } else {
            throw new IllegalArgumentException("Invalid post type");
        }

        String userRegionName = RegionUtil.extractEupMyeonDong(nearbyPostRequestDto.getRegionName());

        List<PostDto> postDTOs = posts.stream()
                .map(post -> {
                    double distanceToPost = calculateDistanceToPost(post, nearbyPostRequestDto.getLatitude(), nearbyPostRequestDto.getLongitude());
                    List<Comment> comments = commentRepository.findByPostId(post.getId());
                    return PostDto.of(post, distanceToPost, comments);
                })
                .collect(Collectors.toList());

        NearbyPostResponseDto responseDto = NearbyPostResponseDto.of(userRegionName, postDTOs);
        return ApiResponse.onGetSuccess(responseDto);
    }

    private double calculateDistanceToPost(Post post, double latitude, double longitude) {
        double postLatitude = post.getLatitude();
        double postLongitude = post.getLongitude();
        return RegionUtil.calculateDistanceBetweenCoordinates(postLatitude, postLongitude, latitude, longitude);
    }

    @Override
    @Transactional
    public ApiResponse<List<DomesticPostDto>> getRecentDomesticPosts(boolean excludeCompleted) {
        List<Post> posts;

        if (excludeCompleted) {
            posts = postRepository.findRecentDomesticPostsExcludingCompleted(PageRequest.of(0, 10));
        } else {
            posts = postRepository.findRecentDomesticPosts(PageRequest.of(0, 10));
        }

        List<DomesticPostDto> postDTOs = posts.stream()
                .map(post -> {
                    List<Comment> comments = commentRepository.findByPostId(post.getId()); // 댓글 목록 조회
                    return DomesticPostDto.of(post, comments); // 댓글을 함께 전달
                })
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(postDTOs);
    }

    @Override
    @Transactional
    public ApiResponse<List<DomesticPostDto>> getTopDomesticPosts(boolean excludeCompleted) {
        List<Post> posts;

        if (excludeCompleted) {
            posts = postRepository.findTopDomesticPostsExcludingCompleted(PageRequest.of(0, 10));
        } else {
            posts = postRepository.findTopDomesticPosts(PageRequest.of(0, 10));
        }

        List<DomesticPostDto> postDTOs = posts.stream()
                .map(post -> {
                    List<Comment> comments = commentRepository.findByPostId(post.getId()); // 댓글 목록 조회
                    return DomesticPostDto.of(post, comments); // 댓글을 함께 전달
                })
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(postDTOs);
    }

    @Override
    @Transactional
    public ApiResponse<Void> changeToPostCompleted(long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        post.setStatus(PostStatus.COMPLETED);

        postRepository.save(post);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    @Transactional
    public ApiResponse<NearbyPostResponseDto> searchNearbyPostsByRecent(String query, NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted) {
        return getNearbyPosts(nearbyPostRequestDto, page, RECENT, excludeCompleted, query);
    }

    @Override
    @Transactional
    public ApiResponse<NearbyPostResponseDto> searchNearbyPostsByTop(String query, NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted) {
        return getNearbyPosts(nearbyPostRequestDto, page, TOP, excludeCompleted, query);
    }

    @Override
    @Transactional
    public ApiResponse<NearbyPostResponseDto> searchNearbyPostsByDistance(String query, NearbyPostRequestDto nearbyPostRequestDto, int page, boolean excludeCompleted) {
        return getNearbyPosts(nearbyPostRequestDto, page, DISTANCE, excludeCompleted, query);
    }

    private ApiResponse<NearbyPostResponseDto> getNearbyPosts(NearbyPostRequestDto nearbyPostRequestDto, int page, int type, boolean excludeCompleted, String query) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        long regionId = regionUtil.convertRegionNameToRegionId(nearbyPostRequestDto.getRegionName());
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);

        List<Post> posts;

        if (query == null || query.isEmpty()) {
            // query가 없는 경우 기본 검색
            if (type == RECENT) {
                posts = excludeCompleted
                        ? postRepository.findRecentPostsExcludingCompleted(startDate, regionId, pageable)
                        : postRepository.findRecentPosts(startDate, regionId, pageable);
            } else if (type == TOP) {
                posts = excludeCompleted
                        ? postRepository.findTopNearPostsExcludingCompleted(startDate, regionId, pageable)
                        : postRepository.findTopNearPosts(startDate, regionId, pageable);
            } else if (type == DISTANCE) {
                posts = excludeCompleted
                        ? postRepository.findDistanceNearPostsExcludingCompleted(regionId, nearbyPostRequestDto.getLongitude(), nearbyPostRequestDto.getLatitude(), pageable)
                        : postRepository.findDistanceNearPosts(regionId, nearbyPostRequestDto.getLongitude(), nearbyPostRequestDto.getLatitude(), pageable);
            } else {
                throw new IllegalArgumentException("Invalid post type");
            }
        } else {
            // query가 있는 경우 검색
            if (type == RECENT) {
                posts = postRepository.searchPostsByKeywordRecentNearby(query, regionId, excludeCompleted, pageable);
            } else if (type == TOP) {
                posts = postRepository.searchPostsByKeywordTopNearby(query, regionId, excludeCompleted, pageable);
            } else if (type == DISTANCE) {
                posts = postRepository.searchPostsByKeywordDistance(query, regionId, excludeCompleted, nearbyPostRequestDto.getLongitude(), nearbyPostRequestDto.getLatitude(), pageable);
            } else {
                throw new IllegalArgumentException("Invalid post type");
            }
        }

        String userRegionName = RegionUtil.extractEupMyeonDong(nearbyPostRequestDto.getRegionName());

        List<PostDto> postDTOs = posts.stream()
                .map(post -> {
                    double distanceToPost = calculateDistanceToPost(post, nearbyPostRequestDto.getLatitude(), nearbyPostRequestDto.getLongitude());
                    List<Comment> comments = commentRepository.findByPostId(post.getId());
                    return PostDto.of(post, distanceToPost, comments);
                })
                .collect(Collectors.toList());

        NearbyPostResponseDto responseDto = NearbyPostResponseDto.of(userRegionName, postDTOs);
        return ApiResponse.onGetSuccess(responseDto);
    }

    @Override
    @Transactional
    public ApiResponse<List<DomesticPostDto>> searchDomesticPostsByRecent(String query, boolean excludeCompleted) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        List<Post> posts = postRepository.searchPostsByKeywordRecentDomestic(query, excludeCompleted, pageable);

        List<DomesticPostDto> postDTOs = posts.stream()
                .filter(post -> post.getHearts() >= 10)

                .map(post -> {
                    List<Comment> comments = commentRepository.findByPostId(post.getId());
                    return DomesticPostDto.of(post, comments);
                })
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(postDTOs);
    }

    @Override
    @Transactional
    public ApiResponse<List<DomesticPostDto>> searchDomesticPostsByTop(String query, boolean excludeCompleted) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        List<Post> posts = postRepository.searchPostsByKeywordTopDomestic(query, excludeCompleted, pageable);

        List<DomesticPostDto> postDTOs = posts.stream()
                .filter(post -> post.getHearts() >= 10)

                .map(post -> {
                    List<Comment> comments = commentRepository.findByPostId(post.getId());
                    return DomesticPostDto.of(post, comments);
                })
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(postDTOs);
    }

    @Override
    @Transactional
    public ApiResponse<NearbyPostResponseDto> getTop5NearbyPosts(NearbyPostRequestDto nearbyPostRequestDto) {
        Pageable pageable = PageRequest.of(0, 5); // 상위 5개의 글만 가져옴

        long regionId;
        try {
            // 입력받은 지역 이름을 사용하여 ID 변환
            regionId = regionUtil.convertRegionNameToRegionId(nearbyPostRequestDto.getRegionName());
        } catch (EntityNotFoundException e) {
            return ApiResponse.onFailure("COMMON500", "Region not found: " + nearbyPostRequestDto.getRegionName(), null);
        }

        LocalDateTime startDate = LocalDateTime.now().minusDays(3);

        // 확인순으로 5개의 게시물 조회
        List<Post> posts = postRepository.findTopNearPostsExcludingCompleted(startDate, regionId, pageable);

        if (posts.isEmpty()) {
            return ApiResponse.onFailure("COMMON404", "No posts found in the region", null);
        }

        String userRegionName = RegionUtil.extractEupMyeonDong(nearbyPostRequestDto.getRegionName());

        // 조회된 게시물들을 DTO로 변환
        List<PostDto> postDTOs = posts.stream()
                .map(post -> {
                    double distanceToPost = calculateDistanceToPost(post, nearbyPostRequestDto.getLatitude(), nearbyPostRequestDto.getLongitude());
                    List<Comment> comments = commentRepository.findByPostId(post.getId());
                    return PostDto.of(post, distanceToPost, comments);
                })
                .collect(Collectors.toList());

        NearbyPostResponseDto responseDto = NearbyPostResponseDto.of(userRegionName, postDTOs);
        return ApiResponse.onGetSuccess(responseDto);
    }

}