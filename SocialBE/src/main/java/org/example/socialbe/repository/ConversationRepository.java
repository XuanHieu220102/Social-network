package org.example.socialbe.repository;

import jakarta.transaction.Transactional;
import org.example.socialbe.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, String> {
    @Query(value = "SELECT count(cp1) > 0 FROM ConversationParticipantEntity cp1 " +
            "JOIN ConversationEntity c ON c.id = cp1.conversationId " +
            "WHERE cp1.userId =:userId1 " +
            "AND c.type = 'PRIVATE' " +
            "AND EXISTS (" +
            "   SELECT 1 FROM ConversationParticipantEntity cp2 WHERE cp2.conversationId = cp1.conversationId " +
            "AND cp2.userId =:userId2)")
    Boolean isConversationExist(String userId1, String userId2);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO conversations(id, type, name) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void createConversation(String id, String type, String name);
}
