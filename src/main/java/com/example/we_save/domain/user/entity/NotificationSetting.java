package com.example.we_save.domain.user.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isNearbyDisaster;

    @Column(nullable = false)
    private boolean isHeartRegionDisaster;

    @Column(nullable = false)
    private boolean isConfirmed;

    @Column(nullable = false)
    private boolean isFlaggedFalse;

    @Column(nullable = false)
    private boolean isReceiveComment;

    @Column(nullable = false)
    private boolean isReceiveReply;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
