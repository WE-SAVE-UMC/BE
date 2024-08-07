package com.example.we_save.domain.region.repository;

import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EupmyeondongRepository extends JpaRepository<EupmyeondongRegion, Long> {
    //List<EupmyeondongRegion> findAllByHeartRegionIdIn(List<Long> heartRegionIds);
    Optional<EupmyeondongRegion> findByRegionFullName(String regionName);
}
