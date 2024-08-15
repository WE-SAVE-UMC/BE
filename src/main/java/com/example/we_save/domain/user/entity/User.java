package com.example.we_save.domain.user.entity;

import com.example.we_save.image.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, length = 11)
    private String phoneNum;

    @Column(nullable = false, length = 16)
    private String nickname;

    @Column(nullable = false)
    private String password;

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @OneToOne
    @JoinColumn(name = "notificationsettingId")
    private NotificationSetting notificationSetting;

    @OneToOne
    @JoinColumn(name="imageId")
    private Image profileImage;


    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Block> blockUserList  = new ArrayList<>();

}
