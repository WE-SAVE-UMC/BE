package com.example.we_save.domain.ads.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import com.example.we_save.domain.ads.entity.AdsOption;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

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
    private final List<AdsOption> options = new ArrayList<>();

}
