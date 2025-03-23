package org.example.socialbe.service;

import jakarta.annotation.Resource;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.notification.request.NotificationRequest;
import org.example.socialbe.dto.notification.response.NotificationInterfaceResponse;
import org.example.socialbe.dto.notification.response.NotificationResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.util.List;

@Slf4j
@Service
public class NotificationService {
    @Resource
    private NotificationRepository notificationRepository;

    public List<NotificationInterfaceResponse> getNotificationsByUserId(UserEntity user, NotificationRequest request) {
        try {
            return notificationRepository.findNotificationsBySenderId(user.getId(), request.getSize(), request.getPage());
        }catch (Exception e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }
}
