package com.example.we_save.domain.post.controller.response;

import com.example.we_save.domain.post.entity.Category;
import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
}
