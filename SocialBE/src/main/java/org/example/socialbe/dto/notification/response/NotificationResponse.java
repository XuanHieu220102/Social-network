package org.example.socialbe.dto.notification.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private String id;
    private String postId;
    private String senderId;
    private String senderUsername;
    private String senderAvatarUrl;
    private String receiverId;
    private String receiverUsername;
    private String receiverAvatarUrl;
    private String type;
    private boolean isRead;
    private String message;
    private LocalDateTime createdAt;
}
