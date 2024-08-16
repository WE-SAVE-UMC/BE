package com.example.we_save.domain.notification.entity;

import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 알림을 받을 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 관련 게시물

    @Column(name = "comment_id", nullable = true)
    private Long commentId; // 댓글 ID (댓글 알림의 경우)

    @Column(name = "button_type", nullable = true)
    private String buttonType; // 버튼 종류 (확인했어요, 허위예요 등)

    @Column(name = "title", nullable = true)
    private String title; // 게시물 제목 (필요한 경우)

    @Column(name = "expiry_time", nullable = true)
    private LocalDateTime expiryTime; // 게시물 작성 후 1시간 후의 시간

    @Column(name = "confirm_count", nullable = true)
    private Integer confirmCount; // 확인했어요 버튼이 눌린 횟수

    @Column(name = "content", nullable = true)
    private String content; // 댓글 내용 (댓글 알림의 경우)

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private boolean isRead = false; // 알림의 읽음 상태

    @Column(name = "is_completed", nullable = true)
    private Boolean isCompleted; // 상황 종료 여부 (상황 종료 알림의 경우)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 알림 생성 시간

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Boolean isCompleted() {
        return this.isCompleted;
    }

}
