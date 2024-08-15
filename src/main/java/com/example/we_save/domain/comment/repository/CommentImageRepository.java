package com.example.we_save.domain.comment.repository;

import com.example.we_save.domain.comment.entity.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentImageRepository extends JpaRepository<CommentImage, Long> {
    public void deleteByCommentId(Long postId);
}
