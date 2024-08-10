package com.example.we_save.domain.notification.repository;

import com.example.we_save.domain.notification.entity.NotificationButton;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationButtonRepository extends JpaRepository<NotificationButton, Long> {

    // 특정 사용자 ID와 게시물 ID로 버튼 알림이 존재하는지 확인하는 메서드
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    // 특정 게시물에 대한 버튼 알림의 개수를 세는 메서드
    int countByPostId(Long postId);

    // 특정 게시물과 버튼 타입으로 버튼 알림이 존재하는지 확인하는 메서드
    boolean existsByPostIdAndButtonType(Long postId, String buttonType);
}
