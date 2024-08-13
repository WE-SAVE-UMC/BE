package com.example.we_save.domain.region.repository;

import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import com.example.we_save.domain.region.entity.HeartRegion;
import com.example.we_save.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HeartRegionRepository extends JpaRepository<HeartRegion, Long> {

    @Query("SELECT h FROM HeartRegion h JOIN FETCH h.region WHERE h.user = :user")
    List<HeartRegion> findAllByUser(@Param("user") User user);
    Optional<HeartRegion> findByUserAndRegion(User user, EupmyeondongRegion region);
}
