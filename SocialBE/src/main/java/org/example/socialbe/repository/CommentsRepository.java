package org.example.socialbe.repository;

import org.example.socialbe.entity.CommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsEntity, String> {
    @Query(value = "SELECT * FROM comments WHERE post_id =:postId AND deleted = false", nativeQuery = true)
    List<CommentsEntity> findByPostId(@Param("postId") String postId);

    @Query(value = """
        SELECT COUNT(*) + COALESCE(SUM(jsonb_array_length(c.reply)), 0)
        FROM comments c
        WHERE c.post_id = :postId
        """, nativeQuery = true)
    Long countCommentsByPostId(@Param("postId") String postId);
}
