//package com.example.we_save.domain.notification.entity;
//
//import com.example.we_save.domain.post.entity.Post;
//import com.example.we_save.domain.user.entity.User;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "notification_status")
//public class NotificationStatus {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user; // 알림을 받을 사용자
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id", nullable = false)
//    private Post post; // 알림과 연관된 게시물
//
//    @Column(name = "title", nullable = false)
//    private String title; // 게시물 제목
//
//    @Column(name = "expiry_time", nullable = false)
//    private LocalDateTime expiryTime; // 기준 시간 (게시물 작성 이후 1시간)
//
//    @Builder.Default
//    @Column(name = "is_read", nullable = false)
//    private boolean isRead = false; // 알림 읽음 상태
//
//    @Column(name = "created_at", nullable = false)
//    private LocalDateTime createdAt; // 알림 생성 시간
//
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }
//}
