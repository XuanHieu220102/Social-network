package org.example.socialbe.repository;

import org.example.socialbe.entity.FriendShipEntity;
import org.example.socialbe.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendShipsRepository extends JpaRepository<FriendShipEntity, String>
{
    @Query("SELECT f FROM FriendShipEntity f " +
            "WHERE (f.userId = :userId AND f.friendId = :friendId) " +
            "   OR (f.userId = :friendId AND f.friendId = :userId) " +
            "ORDER BY f.createdAt DESC")
    List<FriendShipEntity> findFriendshipBetweenUsers(@Param("userId") String userId,
                                                      @Param("friendId") String friendId);


    @Query("SELECT f FROM FriendShipEntity f WHERE f.userId = :userId AND f.friendId = :friendId")
    Optional<FriendShipEntity> findByUserIdAndFriendId(@Param("userId") String userId, @Param("friendId") String friendId);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN FriendShipEntity f ON u.id = f.userId " +  // Người gửi lời mời
            "WHERE f.friendId = :currentUserId " +            // Người nhận là bạn
            "AND f.status = :status")                         // Trạng thái là PENDING
    List<UserEntity> findUsersSentFriendRequests(@Param("currentUserId") String currentUserId, @Param("status") int status);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN FriendShipEntity f ON u.id = f.userId " +  // Người gửi lời mời
            "WHERE f.friendId = :currentUserId " +            // Người nhận là bạn
            "AND f.status = :status " +
            "ORDER BY f.updatedAt DESC LIMIT 2")                         // Trạng thái là PENDING
    List<UserEntity> findNewUsersSentFriendRequests(@Param("currentUserId") String currentUserId, @Param("status") int status);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN FriendShipEntity f ON (u.id = f.userId OR u.id = f.friendId) " +
            "WHERE (f.userId = :currentUserId OR f.friendId = :currentUserId) " +
            "AND f.status = 2 " +
            "AND u.id <> :currentUserId")
    List<UserEntity> findFriends(@Param("currentUserId") String currentUserId);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN FriendShipEntity f ON u.id = f.friendId " +
            "WHERE f.userId = :currentUserId AND f.status = 3")
    List<UserEntity> findBlockedUsers(@Param("currentUserId") String currentUserId);

}
