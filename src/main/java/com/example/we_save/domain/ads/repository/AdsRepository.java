package com.example.we_save.domain.ads.repository;

import com.example.we_save.domain.ads.entity.Ads;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdsRepository extends JpaRepository<Ads, Long> {
}
