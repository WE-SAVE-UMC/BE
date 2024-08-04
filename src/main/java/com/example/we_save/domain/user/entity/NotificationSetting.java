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

    @Column(name = "nearby_disaster", nullable = false)
    private boolean isNearbyDisaster;

    @Column(name = "heartregion_disaster", nullable = false)
    private boolean isHeartRegionDisaster;

    @Column(name = "confirmed", nullable = false)
    private boolean isConfirmed;

    @Column(name =" flagged_false", nullable = false)
    private boolean isFlaggedFalse;

    @Column(name = "receive_comment", nullable = false)
    private boolean isReceiveComment;

    @Column(name = "receive_reply", nullable = false)
    private boolean isReceiveReply;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
