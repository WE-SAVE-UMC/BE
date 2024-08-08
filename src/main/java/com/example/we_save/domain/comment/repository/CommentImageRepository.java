package com.example.we_save.domain.comment.repository;

import com.example.we_save.domain.comment.entity.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentImageRepository extends JpaRepository<CommentImage, Long> {
    void deleteByCommentId(Long commentId);
}
