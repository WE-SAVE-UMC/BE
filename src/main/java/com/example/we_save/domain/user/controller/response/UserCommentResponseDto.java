package com.example.we_save.domain.user.controller.response;

import com.example.we_save.apiPayload.util.RegionUtil;
import com.example.we_save.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCommentResponseDto {

    private long commentId;
    private String content;
    private String regionName;
    private String imageUrl;

    public static UserCommentResponseDto of(Comment comment) {

        String imageUrl = comment.getPost().getImages().isEmpty() ? null : comment.getPost().getImages().get(0).getImageUrl();

        return UserCommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .regionName(RegionUtil.extractRegionAfterSecondSpace(comment.getPost().getPostRegionName()))
                .imageUrl(imageUrl)
                .build();
    }
}
