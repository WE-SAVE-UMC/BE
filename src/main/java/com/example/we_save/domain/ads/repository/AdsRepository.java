package com.example.we_save.domain.ads.repository;

import com.example.we_save.domain.ads.entity.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdsRepository extends JpaRepository<Ads, Long> {

    @Query(value = "SELECT * FROM ads ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Ads> findRandomAD();
}
