package org.example.socialbe.repository;

import org.example.socialbe.dto.likes.response.GetUserLikePostResponse;
import org.example.socialbe.entity.LikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<LikesEntity, String> {
    @Query(value = "SELECT * FROM likes WHERE user_id = :userId AND post_id = :postId", nativeQuery = true)
    LikesEntity findLikesByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);


    @Query(value = "SELECT count(*) FROM likes WHERE post_id =:postId AND status = 1", nativeQuery = true)
    Long countLikesByPostId(@Param("postId") String postId);

    @Query(value = "SELECT users.id, users.username, users.avatar_url " +
            "FROM likes " +
            "INNER JOIN users ON likes.user_id = users.id " +
            "WHERE likes.post_id = :postId",
            nativeQuery = true)
    List<GetUserLikePostResponse.UserLikePost> findUsersLikesByPostId(@Param("postId") String postId);
}
