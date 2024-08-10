package com.example.we_save.domain.ads.repository;

import com.example.we_save.domain.ads.entity.AdsOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdsOptionRepository extends JpaRepository<AdsOption, Long> {
    void deleteAds(Long adId);

    void deleteByAdsId(Long adId);
}
