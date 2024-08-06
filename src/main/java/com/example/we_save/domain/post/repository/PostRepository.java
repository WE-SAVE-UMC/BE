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
    @Query("SELECT p FROM Post p WHERE p.createdAt >= :startDate AND p.areaId = :areaId ORDER BY p.createdAt DESC")
    List<Post> findRecentPosts(@Param("startDate") LocalDateTime startDate,
                               @Param("areaId") Long areaId,
                               Pageable pageable);
}
