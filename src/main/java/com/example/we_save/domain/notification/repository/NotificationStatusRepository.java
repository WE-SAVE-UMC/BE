package com.example.we_save.domain.notification.repository;

import com.example.we_save.domain.notification.entity.NotificationStatus;
import com.example.we_save.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {

    List<NotificationStatus> findByUserOrderByCreatedAtDesc(User user);
}