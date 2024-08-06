package com.example.we_save.domain.region.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SidoRegion extends BaseEntity {

    @Column(nullable = false)
    private String regionCode;

    @Column(nullable = false)
    private String regionName;
}
