package com.example.we_save.domain.notification.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_buttons")
public class NotificationButton extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 알림을 받을 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 알림이 발생한 게시물

    @Column(name = "button_type", nullable = false)
    private String buttonType; // 버튼 유형 (확인했어요, 허위예요)

    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false; // 알림 읽음 상태

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 알림 생성 시간

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
