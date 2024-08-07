package com.example.we_save.domain.region.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SigunguRegion extends BaseEntity {

    @Column(nullable = false)
    private String regionCode;

    @Column(nullable = false)
    private String regionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private SidoRegion sido;
}
