package org.example.socialbe.repository;

import org.example.socialbe.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, String> {
    @Query("SELECT MAX(m.createdAt) FROM MessageEntity m WHERE m.conversationId = :conversationId")
    LocalDateTime findLastMessageTimeByConversationId(String conversationId);
}
