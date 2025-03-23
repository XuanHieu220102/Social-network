package org.example.socialbe.dto.stories.response;

import java.time.LocalDateTime;

public interface StoriesInterfaceResponse {
    String getId();
    String getUserId();
    String getUsername();
    String getAvatarUrl();
    String getContent();
    String getVideoUrl();
    String getBackgroundColor();
    boolean getIsActive();
    String getType();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getExpiresAt();
    boolean getIsValid();
}
