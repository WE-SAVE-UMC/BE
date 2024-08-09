package com.example.we_save.domain.post.repository;

import com.example.we_save.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 생성 시간 기준으로 최근 N개의 게시물 조회
    @Query("SELECT p FROM Post p WHERE p.createAt >= :startDate AND p.region.id = :regionId ORDER BY p.createAt DESC")
    List<Post> findRecentPosts(@Param("startDate") LocalDateTime startDate,
                               @Param("regionId") Long regionId,
                               Pageable pageable);

    // 인기순으로 N개의 게시물 조회
    @Query("SELECT p FROM Post p WHERE p.createAt >= :startDate ORDER BY p.hearts DESC")
    List<Post> findTopPosts(@Param("startDate") LocalDateTime startDate,
                               Pageable pageable);
}
