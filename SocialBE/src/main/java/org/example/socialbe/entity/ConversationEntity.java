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
@Table(name = "conversations", indexes = {
        @Index(name = "idx_conversations_type", columnList = "type"),
        @Index(name = "idx_conversations_created_at", columnList = "created_at")
})
public class ConversationEntity {
    @Id
    @GeneratedValue(generator = "mygen")
    @GenericGenerator(name = "mygen", strategy = "org.example.socialbe.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "type", nullable = false)
    private String type; // "PRIVATE" (chat 1-1) hoặc "GROUP" (chat nhóm)

    @Column(name = "name")
    private String name; // Tên nhóm (dùng cho chat nhóm, có thể để null nếu là chat 1-1)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}