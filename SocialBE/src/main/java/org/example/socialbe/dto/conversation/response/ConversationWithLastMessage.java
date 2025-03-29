package org.example.socialbe.dto.conversation.response;

import java.time.LocalDateTime;


public interface ConversationWithLastMessage {
    String getId(); // null nếu là hội thoại tiềm năng
    String getType(); // "PRIVATE" hoặc "GROUP"
    String getName(); // null cho PRIVATE chat
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    String getLastMessage(); // null nếu chưa có tin nhắn
    LocalDateTime getLastMessageTime();
    String getLastSenderId();
    Integer getUnreadCount();
    String getAvatarUrl();
    String getOtherUserId(); // ID người tham gia khác (bạn bè)
    boolean isPotential();
}
