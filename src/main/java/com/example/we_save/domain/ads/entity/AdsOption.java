package com.example.we_save.domain.ads.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AdsOption extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ads_id", nullable = false)
    private Ads ads;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean isCorrect;

    @Column(nullable = false)
    private String responseText;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String redirectUrl;
}
