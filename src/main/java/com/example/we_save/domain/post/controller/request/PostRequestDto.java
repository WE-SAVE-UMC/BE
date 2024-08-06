package com.example.we_save.domain.post.controller.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private String status;
    private double longitude;
    private double latitude;
    private String postRegionName;
    private List<String> images;
    private boolean report119;

}
