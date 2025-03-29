package org.example.socialbe.dto.conversation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private String id; // null nếu là cuộc hội thoại chưa tạo
    private String type; // "PRIVATE" hoặc "GROUP"
    private String name;

    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Integer unreadCount;
    private String avatarUrl;

    // Thông tin cho cuộc hội thoại chưa tạo
    private boolean isPotential;
    private String potentialFriendId;
    private String potentialFriendName;
}