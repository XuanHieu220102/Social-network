package org.example.socialbe.repository;

import jakarta.transaction.Transactional;
import org.example.socialbe.entity.ConversationParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipantEntity, String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO conversation_participants(id, conversation_id, user_id) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void insertConversationParticipants(String id, String conversationId, String userId);
}
