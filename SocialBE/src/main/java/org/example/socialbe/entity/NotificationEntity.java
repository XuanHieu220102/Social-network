package org.example.socialbe.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(generator = "mygen")
    @GenericGenerator(name = "mygen", strategy = "org.example.socialbe.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "receive_id", nullable = false)
    private String receiveId;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "message", nullable = false)
    private String message;

    @Type(JsonType.class)
    @Column(name = "data", columnDefinition = "jsonb")
    private Object data;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
