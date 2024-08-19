package com.example.we_save.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private String postTitle;
    private String notificationType;
    private String username;
    private String buttonType;
    private Integer buttonTotalCount;
    private LocalDateTime timestamp;

    // 생성자 및 필요한 메소드들
    public Notification(Long postId, String postTitle, String notificationType, String username, String buttonType, Integer buttonTotalCount) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.notificationType = notificationType;
        this.username = username;
        this.buttonType = buttonType;
        this.buttonTotalCount = buttonTotalCount;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public Integer getButtonTotalCount() {
        return buttonTotalCount;
    }

    public void setButtonTotalCount(Integer buttonTotalCount) {
        this.buttonTotalCount = buttonTotalCount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}



//package com.example.we_save.domain.notification.entity;
//
//import com.example.we_save.domain.post.entity.Post;
//import com.example.we_save.domain.user.entity.User;
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "notification")
//public class Notification {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;  // 알림을 받는 사용자
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id", nullable = false)
//    private Post post;  // 알림이 관련된 게시물
//
//    @Column(nullable = false)
//    private String notificationType;  // 알림 유형
//
//    @Column(nullable = true)
//    private String buttonType;  // 버튼 유형
//
//    @Column(nullable = true)
//    private Integer buttonTotalCount;  // 버튼이 눌린 총 횟수
//
//    @Column(nullable = false)
//    private LocalDateTime timestamp;  // 알림 생성 시간
//
//    @PrePersist
//    protected void onCreate() {
//        this.timestamp = LocalDateTime.now();
//    }
//}
