package com.example.we_save.domain.post.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostHomeResponseDto {

    private long postId;
    private long areaId;
    private double distance;
    private int confirms;
    private LocalDateTime createAt;

    private long postImageId;
    private String imageUrl;

    private long categoryId;
    private String categoryName;

    // TODO: 정적메서드 of 구현
    /*
    public static PostHomeResponseDto of() {

    }*/
}
