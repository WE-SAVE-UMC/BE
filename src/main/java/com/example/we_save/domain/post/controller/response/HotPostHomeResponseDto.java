package com.example.we_save.domain.post.controller.response;

import com.example.we_save.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotPostHomeResponseDto {

    private long postId;
    private long areaId;
    private String title;
    private double hearts;
    private LocalDateTime createAt;

    private String imageUrl;

    public static HotPostHomeResponseDto of(Post post) {

        return HotPostHomeResponseDto.builder()
                .postId(post.getId())
                // TODO: AreaID
                .title(post.getTitle())
                .hearts(post.getHearts())
                .createAt(post.getCreatedAt())
                .imageUrl(post.getImages().isEmpty() ?  null : post.getImages().get(0))
                .build();
    }
}
