package org.example.socialbe.repository;

import org.example.socialbe.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {
    PostEntity findByIdAndDeletedFalse(String id);
    @Query(value = "SELECT count(p) FROM PostEntity p WHERE p.id =:postId AND p.type = 'SHARED'")
    long countSharedPost(@Param("postId") String postId);

    @Query(value = """
        SELECT p.* FROM posts p
        WHERE p.user_id = :userId
            AND p.status <> 1
            AND p.privacy = 2
        
        UNION 
        
        SELECT p.* FROM posts p
        WHERE p.user_id IN (
            SELECT CASE 
                WHEN f.user_id =:userId THEN f.friend_id
                WHEN f.friend_id =:userId THEN f.user_id
                ELSE null
            END
            FROM friendships f 
            WHERE f.status = 2 AND (f.user_id = :userId OR f.friend_id = :userId)
        )
        AND p.privacy = 2
        AND p.status <> 1
        
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<PostEntity> findPostsByUserAndFriends(
            @Param("userId") String userId,
            @Param("limit") int limit,
            @Param("offset") int offset);

}
