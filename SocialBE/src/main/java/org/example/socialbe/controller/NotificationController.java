package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.notification.request.NotificationRequest;
import org.example.socialbe.dto.notification.response.NotificationInterfaceResponse;
import org.example.socialbe.dto.notification.response.NotificationResponse;
import org.example.socialbe.entity.UserEntity;
import org.example.socialbe.service.NotificationService;
import org.example.socialbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    @Resource
    private NotificationService notificationService;

    @Resource
    private UserService userService;

    @PostMapping("/current-user")
    public List<NotificationInterfaceResponse> currentUserNotifications(@RequestBody NotificationRequest request) {
        UserEntity user = userService.checkAuthentication();
        log.info("User {} get notifications", user.getId());
        return notificationService.getNotificationsByUserId(user, request);
    }
}
