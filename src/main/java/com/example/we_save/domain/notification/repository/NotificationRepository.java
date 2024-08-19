package com.example.we_save.domain.notification.repository;

import com.example.we_save.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}


//public interface NotificationRepository extends JpaRepository<Notification, Long> {
//
//    // 특정 사용자의 모든 알림을 생성 시간 역순으로 조회
//    @Query("SELECT n FROM Notification n WHERE n.user = :user ORDER BY n.createdAt DESC")
//    List<Notification> findByUserOrderByCreatedAtDesc(@Param("user") User user);
//
//    // 특정 사용자에 대한 특정 알림 ID로 조회
//    Optional<Notification> findByIdAndUser(Long id, User user);
//}