package org.example.socialbe.dto.message.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private String conversationId;
    private String senderId;
    private String content;
    private LocalDateTime createdAt;
}
