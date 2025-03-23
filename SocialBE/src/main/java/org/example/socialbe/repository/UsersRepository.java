package org.example.socialbe.repository;

import org.apache.catalina.User;
import org.example.socialbe.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String> {
    @Query("SELECT CASE WHEN COUNT(user) > 0 THEN true ELSE false END FROM UserEntity user WHERE user.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(user) > 0 THEN true ELSE false END FROM UserEntity user WHERE user.username = :username")
    boolean existsByUsername(@Param("username") String username);
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);

    @Query("SELECT u FROM UserEntity u " +
            "LEFT JOIN FriendShipEntity f ON u.id = f.friendId AND f.userId = :currentUserId " +
            "WHERE (f.status IS NULL OR f.status = 4) " +
            "AND u.id <> :currentUserId")
    List<UserEntity> findNonFriendsOrRejected(@Param("currentUserId") String currentUserId);

}
