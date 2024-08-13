package com.example.we_save.domain.ads.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ads extends BaseEntity {

    @Column(nullable = false)
    private String question;

    @OneToMany(mappedBy = "ads", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdsOption> options = new ArrayList<>();
}
