package com.example.we_save.domain.region.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EupmyeondongRegion extends BaseEntity {

    @Column(nullable = false)
    private String regionCode;

    @Column(nullable = false)
    private String regionName;

    @Column(nullable = false, length = 30)
    private String regionFullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id")
    private SidoRegion sido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sigungu_id")
    private SigunguRegion sigungu;
}
