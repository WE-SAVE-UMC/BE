package com.example.we_save.domain.region.repository;

import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import com.example.we_save.domain.region.entity.HeartRegion;
import com.example.we_save.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRegionRepository extends JpaRepository<HeartRegion, Long> {

    List<HeartRegion> findAllByUser(User user);
    Optional<HeartRegion> findByUserAndRegion(User user, EupmyeondongRegion region);
}
