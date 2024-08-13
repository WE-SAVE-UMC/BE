package com.example.we_save.domain.comment.repository;

import com.example.we_save.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    void deleteByPostId(Long postId);
    @Query("SELECT c FROM Comment c JOIN FETCH c.post WHERE c.user.id = :userId ORDER BY c.createAt DESC")
    List<Comment> findAllByUserId(long userId);
}
