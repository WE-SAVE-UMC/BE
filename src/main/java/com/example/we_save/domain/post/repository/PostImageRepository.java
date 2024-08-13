package com.example.we_save.domain.post.repository;

import com.example.we_save.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
