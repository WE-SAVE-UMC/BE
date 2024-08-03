package com.example.we_save.domain.user.entity;

import com.example.we_save.global.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicInsert @DynamicUpdate
@Table(name = "t_user")
public class User extends BaseEntity {

    @Column(name = "phone_num", nullable = false, length = 11)
    private String phoneNum;

    @Column(name = "nickname", nullable = false, length = 16)
    private String nickname;

    @Column(name = "password", length = 20)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @OneToOne
    @JoinColumn(name = "notificationsetting_id")
    private NotificationSetting notificationSetting;

    @Column(name = "image_url")
    private String imageUrl;
}
