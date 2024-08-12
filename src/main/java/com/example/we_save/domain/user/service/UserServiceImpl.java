package com.example.we_save.domain.user.service;

import com.example.we_save.apiPayload.ApiResponse;
import com.example.we_save.apiPayload.code.status.SuccessStatus;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.entity.PostStatus;
import com.example.we_save.domain.post.repository.PostRepository;
import com.example.we_save.domain.user.controller.response.UserPostResponseDto;
import com.example.we_save.domain.user.converter.BlockConverter;
import com.example.we_save.domain.user.entity.Block;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.BlockRepository;
import com.example.we_save.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAuthCommandService userAuthService;
    private final PostRepository postRepository;
    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<List<UserPostResponseDto>> getMyPosts() {

        long userId = userAuthService.getAuthenticatedUserInfo().getId();

        List<Post> posts = postRepository.findAllByUser(userId);

        List<UserPostResponseDto> userDtos = posts.stream()
                .map(UserPostResponseDto::of)
                .collect(Collectors.toList());

        return ApiResponse.onGetSuccess(userDtos);
    }

    @Override
    @Transactional
    public ApiResponse<Void> updatePostCompleted(long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        post.setStatus(PostStatus.COMPLETED);

        postRepository.save(post);

        return ApiResponse.onPostSuccess(null, SuccessStatus._POST_OK);
    }

    @Override
    public ApiResponse<Void> deletePost(long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        postRepository.delete(post);

        return ApiResponse.onDeleteSuccess(null);
    }

    @Override
    public Block addBlock(User user, long targetId) {
        User targetUser = userRepository.findById(targetId);
        if (targetUser==null){
            throw new IllegalArgumentException("존재하지 않는 유저를 차단할 수 없습니다.");
        }

        // 자기 자신을 차단하려는 경우
        if (user.getId() == targetId) {
            throw new IllegalArgumentException("자기 자신을 차단할 수 없습니다.");
        }
        //이미 차단되어 있는 경우
        Optional<Block> existBlock = blockRepository.findByUserIdAndTargetId(user.getId(),targetId);
        if (existBlock.isPresent()) {
            throw new IllegalArgumentException("이미 차단된 유저 입니다.");
        }
        // 차단되지 않은 경우, 새로운 차단 엔티티를 생성하여 저장
        Block block = BlockConverter.toMakeBlock(user,targetId);
        return blockRepository.save(block);
    }

    @Override
    public void deleteBlock(User user, long targetId) {
        User targetUser = userRepository.findById(targetId);
        if (targetUser==null){
            throw new IllegalArgumentException("존재하지 않는 유저를 차단 해제 할 수 없습니다.");
        }

        // 자기 자신을 차단해제 하려는 경우
        if (user.getId() == targetId) {
            throw new IllegalArgumentException("자기 자신을 차단 해제할 수 없습니다.");
        }
        //차단 해제
        Optional<Block> existBlock = blockRepository.findByUserIdAndTargetId(user.getId(),targetId);
        if (existBlock.isPresent()) {
            Block block = existBlock.get();
            blockRepository.delete(block); //차단 해제
        }
        else{
            throw new IllegalArgumentException("차단되지 않은 유저입니다.");
        }
    }
}
