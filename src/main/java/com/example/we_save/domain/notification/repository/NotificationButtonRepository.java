package com.example.we_save.domain.notification.repository;

import com.example.we_save.domain.notification.entity.NotificationButton;
import com.example.we_save.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationButtonRepository extends JpaRepository<NotificationButton, Long> {

    List<NotificationButton> findByUserOrderByCreatedAtDesc(User user);
}
