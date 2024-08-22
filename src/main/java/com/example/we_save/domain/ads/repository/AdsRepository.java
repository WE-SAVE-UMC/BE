package com.example.we_save.domain.ads.repository;

import com.example.we_save.domain.ads.entity.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdsRepository extends JpaRepository<Ads, Long> {

    @Query(value = "SELECT * FROM Ads ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Ads findRandomAD();
}
