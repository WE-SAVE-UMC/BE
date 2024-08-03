package com.example.we_save.domain.user.entity;

import com.example.we_save.global.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class NotificationSetting extends BaseEntity {

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
}
