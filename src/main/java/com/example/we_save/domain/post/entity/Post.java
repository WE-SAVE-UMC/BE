package com.example.we_save.domain.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false, length = 100)
    private String postRegionName;

    @Column(nullable = false)
    private int hearts;

    @Column(nullable = false)
    private int dislikes;

    @Column(nullable = false)
    private int comments;

    @ElementCollection
    private List<String> images;

    @Column(nullable = false)
    private boolean report119;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int reportCount;
}
