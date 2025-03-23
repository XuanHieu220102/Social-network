package org.example.socialbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conversation_participants", indexes = {
        @Index(name = "idx_conversation_participants_conversation_id", columnList = "conversation_id"),
        @Index(name = "idx_conversation_participants_user_id", columnList = "user_id"),
        @Index(name = "idx_conversation_participants_conversation_user", columnList = "conversation_id, user_id")
})
public class ConversationParticipantEntity {
    @Id
    @GeneratedValue(generator = "mygen")
    @GenericGenerator(name = "mygen", strategy = "org.example.socialbe.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "conversation_id", nullable = false)
    private String conversationId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Column(name = "left_at")
    private LocalDateTime leftAt;
}