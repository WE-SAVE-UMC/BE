package com.example.we_save.domain.user.converter;

import com.example.we_save.domain.user.controller.response.BlockResponseDto;
import com.example.we_save.domain.user.entity.Block;
import com.example.we_save.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BlockConverter {
    public static Block toMakeBlock(User user, long targetId){
        return Block.builder()
                .targetId(targetId)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static BlockResponseDto.BlockResultDto toBlockResultDto(Block block){
        return BlockResponseDto.BlockResultDto.builder()
                .BlockId(block.getId())
                .userId(block.getUser().getId())
                .targetId(block.getTargetId())
                .build();
    }

    public static BlockResponseDto.BlockUserListResultDto toBlockUserListResultDto(List<User> users){
        List<BlockResponseDto.BlockUserDto> blockUserDtos = users.stream()
                .map(user -> BlockResponseDto.BlockUserDto.builder()
                        .userId(user.getId())
                        .nickname(user.getNickname())
                        .profileImage(user.getProfileImage())
                        .build())
                .collect(Collectors.toList());
        return BlockResponseDto.BlockUserListResultDto.builder()
                        .blockUserList(blockUserDtos)
                        .build();
    }
}

