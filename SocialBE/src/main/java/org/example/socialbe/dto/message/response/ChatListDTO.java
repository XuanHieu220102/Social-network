package org.example.socialbe.dto.message.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatListDTO {
    private String conversationId;
    private String displayName;
    private String type;
    private LocalDateTime lastMessageTime;
    private String friendId;
}