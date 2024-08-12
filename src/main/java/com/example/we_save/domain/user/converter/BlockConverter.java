package com.example.we_save.domain.user.converter;

import com.example.we_save.domain.user.controller.response.BlockResponseDto;
import com.example.we_save.domain.user.entity.Block;
import com.example.we_save.domain.user.entity.User;

import java.time.LocalDateTime;

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
                .BlockUserId(block.getId())
                .userId(block.getUser().getId())
                .targetId(block.getTargetId())
                .build();
    }
}

