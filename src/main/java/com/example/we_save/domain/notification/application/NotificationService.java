package com.example.we_save.domain.notification.application;

import com.example.we_save.domain.notification.entity.Notification;
import com.example.we_save.domain.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification saveNotification(Long postId, String postTitle, String notificationType, String username, String buttonType, Integer buttonTotalCount) {
        Notification notification = new Notification(postId, postTitle, notificationType, username, buttonType, buttonTotalCount);
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}





//import com.example.we_save.domain.notification.entity.Notification; // 올바른 Notification 클래스를 import
//import com.example.we_save.domain.notification.repository.NotificationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//
//    @Autowired
//    public NotificationService(NotificationRepository notificationRepository) {
//        this.notificationRepository = notificationRepository;
//    }
//
//    public Notification saveNotification(Long postId, String postTitle, String notificationType, String username, String buttonType, Integer buttonTotalCount) {
//        Notification notification = new Notification(postId, postTitle, notificationType, username, buttonType, buttonTotalCount);
//        return notificationRepository.save(notification);
//    }
//
//    public List<Notification> getAllNotifications() {
//        return notificationRepository.findAll();
//    }
//}


//public interface NotificationService {
//
//    @Transactional(readOnly = true)
//    List<NotificationResponseDto> getNotifications(User user, int page, int size, boolean excludeCompleted);
//
//    // 특정 사용자의 모든 알림 조회
//    ApiResponse<List<NotificationResponseDto>> getNotifications(User user, int page, boolean excludeCompleted);
//
//    // 댓글 알림 생성
//    ApiResponse<Void> createCommentNotification(User user, NotificationRequestDto requestDto);
//
//    // 버튼 알림 생성 (확인했어요, 허위예요)
//    ApiResponse<Void> createButtonNotification(User user, NotificationRequestDto requestDto);
//
//    // 인기 게시글 알림 생성 (확인했어요 버튼 10개 이상)
//    ApiResponse<Void> createPopularNotification(User user, NotificationRequestDto requestDto);
//
//    // 상황 종료 알림 생성
//    ApiResponse<Void> createStatusNotification(User user, NotificationRequestDto requestDto);
//
//    // 알림 읽음 상태로 변경
//    ApiResponse<Void> markNotificationAsRead(User user, Long notificationId);
//
//    SseEmitter streamNotifications(User user);
//}
