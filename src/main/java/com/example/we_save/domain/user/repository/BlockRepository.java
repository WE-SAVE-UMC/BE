package com.example.we_save.domain.user.repository;


import com.example.we_save.domain.user.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findByUserIdAndTargetId(Long userId, Long targetId);
    List<Block> findAllByUserId(Long userId);
}
