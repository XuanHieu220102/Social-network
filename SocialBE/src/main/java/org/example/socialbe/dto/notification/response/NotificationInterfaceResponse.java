package org.example.socialbe.dto.notification.response;

import java.time.LocalDateTime;

public interface NotificationInterfaceResponse {
    String getId();
    String getPostId();
    String getSenderId();
    String getSenderUsername();
    String getSenderAvatarUrl();
    String getReceiverId();
    String getReceiverUsername();
    String getReceiverAvatarUrl();
    String getType();
    boolean getIsRead();
    String getMessage();
    LocalDateTime getCreatedAt();
}
