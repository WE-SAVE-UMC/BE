package com.example.we_save.domain.notification.repository;

import com.example.we_save.domain.notification.entity.NotificationComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationCommentRepository extends JpaRepository<NotificationComment, Long> {

    // 특정 사용자 ID와 게시물 ID로 알림 댓글이 존재하는지 확인하는 메서드
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    // 특정 게시물에 대한 알림 댓글의 개수를 세는 메서드
    int countByPostId(Long postId);
}
