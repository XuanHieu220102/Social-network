package org.example.socialbe.repository;

import org.example.socialbe.dto.notification.response.NotificationInterfaceResponse;
import org.example.socialbe.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
    @Query("""
    SELECT n.id as id,
           n.postId as postId,
           s.id as senderId,
           s.username as senderUsername,
           s.avatarUrl as senderAvatarUrl,
           r.id as receiverId,
           r.username as receiverUsername,
           r.avatarUrl as receiverAvatarUrl,
           n.type as type,
           n.isRead as isRead,
           n.message as message,
           n.createdAt as createdAt
    FROM NotificationEntity n
    LEFT JOIN UserEntity s ON n.senderId = s.id
    LEFT JOIN UserEntity r ON n.receiveId = r.id
    WHERE n.receiveId = :userId
    ORDER BY n.createdAt DESC
    LIMIT :limit OFFSET :offset
""")
    List<NotificationInterfaceResponse> findNotificationsBySenderId(
            @Param("userId") String userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
