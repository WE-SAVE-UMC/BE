package com.example.we_save.domain.user.controller.response;

import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserCommentResponseDto {

    private long commentId;
    private long postId;
    private String title;
    private String status;
    private String category;
    private String regionName;
    private LocalDateTime createAt;
    private String imageUrl;

    public static UserCommentResponseDto of(Comment comment) {

        String imageUrl = comment.getPost().getImages().isEmpty() ? null : comment.getPost().getImages().get(0).getFilePath();

        return UserCommentResponseDto.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .title(comment.getPost().getTitle())
                .status(comment.getPost().toString())
                .category(comment.getPost().getCategory().toString())
                .regionName(RegionUtil.extractRegionAfterSecondSpace(comment.getPost().getPostRegionName()))
                .createAt(comment.getCreateAt())
                .imageUrl(imageUrl)
                .build();
    }
}
