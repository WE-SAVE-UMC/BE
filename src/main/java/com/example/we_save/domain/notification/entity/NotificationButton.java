package com.example.we_save.domain.notification.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NotificationButton extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    // 알림 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    // 유저 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    // 게시글 ID

    @Column(nullable = false)
    private String buttonType;
    // 눌린 버튼의 종류 ('확인했어요' 또는 '허위예요')

    @Column(nullable = false)
    private boolean isRead = false;
    // 알림 읽음 상태

    @Column(nullable = false)
    private LocalDateTime createdAt;
    // 알림 생성 시간

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
