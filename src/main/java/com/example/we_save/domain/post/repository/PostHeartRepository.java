package com.example.we_save.domain.post.repository;

import com.example.we_save.domain.post.entity.PostHeart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
}
