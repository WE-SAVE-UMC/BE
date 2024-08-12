package com.example.we_save.domain.user.controller.response;

import com.example.we_save.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockResponseDto {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlockResultDto {
        Long BlockId;
        Long userId;
        Long targetId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlockUserListResultDto{
        List<BlockUserDto> blockUserListDto;
    }
    @Getter
    @Builder
    public static class BlockUserDto{
        private Long userId;
        private String nickname;
        private String profileImage;
    }


}
