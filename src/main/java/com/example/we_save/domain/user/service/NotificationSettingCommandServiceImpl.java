package com.example.we_save.domain.user.service;

import com.example.we_save.domain.user.converter.NotificationSettingConverter;
import com.example.we_save.domain.user.entity.NotificationSetting;
import com.example.we_save.domain.user.repository.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationSettingCommandServiceImpl implements NotificationSettingCommandService {
    private final NotificationSettingRepository notificationSettingRepository;

    @Override
    public NotificationSetting createNotificationSetting() {
        return notificationSettingRepository.save(NotificationSetting.builder()
                .isNearbyDisaster(true)
                .isHeartRegionDisaster(true)
                .isConfirmed(true)
                .isFlaggedFalse(true)
                .isReceiveComment(true)
                .isReceiveReply(true)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
