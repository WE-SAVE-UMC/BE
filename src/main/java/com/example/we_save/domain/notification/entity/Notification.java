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
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user; // 유저 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 게시글 ID

    @Column(nullable = false, length = 100)
    private String title; // 게시글 제목

    @Column(nullable = false)
    private LocalDateTime postCreatedAt; // 게시글 생성 시간

    @Column(nullable = false)
    private boolean isRead; // 알림 읽음 상태

    @Column(nullable = false)
    private int confirmCount; // "확인했어요" 버튼 개수

//    @Column(nullable = false)
//    private int falseReportCount; // "허위예요" 버튼 개수

    public LocalDateTime getCreatedAt() {
        return this.postCreatedAt;
    }
}
