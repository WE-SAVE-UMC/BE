package com.example.we_save.domain.post.controller.request;

import com.example.we_save.domain.post.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    @NotNull(message = "UserId must not be empty")
    private Long userId;

    @NotNull(message = "Category must not be empty")
    private Category category;

    @NotEmpty(message = "Title name must not be empty")
    private String title;

    @Size(max = 300)
    @NotEmpty(message = "Content must not be empty")
    private String content;

    @NotEmpty(message = "Status must not be empty")
    private String status;

    private double longitude;
    private double latitude;

    @NotEmpty(message = "Post region name must not be empty")
    private String postRegionName;

    private boolean report119;

}