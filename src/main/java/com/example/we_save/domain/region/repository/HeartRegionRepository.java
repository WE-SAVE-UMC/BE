package com.example.we_save.domain.region.repository;

import com.example.we_save.domain.region.entity.HeartRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRegionRepository extends JpaRepository<HeartRegion, Long> {
}
