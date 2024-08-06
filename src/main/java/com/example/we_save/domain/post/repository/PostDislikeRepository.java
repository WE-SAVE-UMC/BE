package com.example.we_save.domain.post.repository;

import com.example.we_save.domain.post.entity.PostDislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDislikeRepository extends JpaRepository<PostDislike, Long> {
    boolean existsByPostIdAndUserId(Long id, Long userId);
    void deleteByPostIdAndUserId(Long id, Long userId);
}
