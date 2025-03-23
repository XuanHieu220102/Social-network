package org.example.socialbe.repository;

import org.example.socialbe.dto.stories.response.StoriesInterfaceResponse;
import org.example.socialbe.entity.StoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoriesRepository extends JpaRepository<StoriesEntity, String> {
    @Query(value = "SELECT s.id AS id, u.id AS userId, u.username AS username, u.avatar_url AS avatarUrl, s.content AS content, s.video_url AS videoUrl, " +
            "s.background_color AS backgroundColor, s.is_active AS isActive, s.type AS type, " +
            "s.created_at AS createdAt, s.updated_at AS updatedAt, s.expires_at AS expiresAt, " +
            "CASE WHEN s.expires_at > NOW() THEN 'true' ELSE 'false' END AS isValid " +
            "FROM stories s " +
            "LEFT JOIN users u ON u.id = s.user_id " +
            "ORDER BY s.created_at DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<StoriesInterfaceResponse> findAllActiveStories(
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
