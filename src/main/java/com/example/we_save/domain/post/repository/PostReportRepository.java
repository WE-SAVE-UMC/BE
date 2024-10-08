package com.example.we_save.domain.post.repository;

import com.example.we_save.domain.post.entity.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    int countByPostId(Long postId);
    void deleteByPostId(Long postId);
}
