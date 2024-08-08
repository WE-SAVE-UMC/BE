package com.example.we_save.domain.user.repository;

import com.example.we_save.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByPhoneNum(String phoneNum);
    Boolean existsByNickname(String nickname);
    List<User> findByPhoneNum(String phoneNum);
    User findById(long id);
}
