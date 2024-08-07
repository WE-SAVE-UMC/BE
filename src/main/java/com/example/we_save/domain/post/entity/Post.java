package com.example.we_save.domain.post.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import com.example.we_save.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private EupmyeondongRegion region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

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

    private int reportCount;
}
