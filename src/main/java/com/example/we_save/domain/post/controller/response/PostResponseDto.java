package com.example.we_save.domain.post.controller.response;

import com.example.we_save.domain.post.entity.Post;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
