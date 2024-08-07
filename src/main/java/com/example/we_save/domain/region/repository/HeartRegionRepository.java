package com.example.we_save.domain.region.repository;

import com.example.we_save.domain.region.entity.HeartRegion;
import com.example.we_save.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRegionRepository extends JpaRepository<HeartRegion, Long> {

    List<HeartRegion> findByUser(User user);
}
