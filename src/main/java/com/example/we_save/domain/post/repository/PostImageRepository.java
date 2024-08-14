package com.example.we_save.domain.post.repository;

import com.example.we_save.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    public List<PostImage> findByPostId(Long postId);
    public void deleteByPostId(Long postId);
}
