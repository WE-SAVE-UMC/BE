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
@Table(name = "notification_comments")
public class NotificationComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 알림을 받을 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 댓글이 달린 게시물

    @Column(name = "comment_id", nullable = false)
    private Long commentId; // 댓글 ID

    @Column(name = "commenter_id", nullable = false)
    private Long commenterId; // 댓글을 단 사용자 ID

    @Column(name = "commenter_name", nullable = false, length = 100)
    private String commenterName; // 댓글을 단 사용자 이름

    @Column(name = "content", nullable = false, length = 500)
    private String content; // 댓글 내용

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
