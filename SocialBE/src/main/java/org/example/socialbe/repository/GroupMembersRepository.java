package org.example.socialbe.repository;

import org.example.socialbe.entity.GroupMembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMembersEntity, String> {
    List<GroupMembersEntity> findByGroupId(String groupId);
    @Query(value = "SELECT gm FROM GroupMembersEntity gm WHERE gm.groupId =:groupId AND gm.userId =:memberId AND gm.status = 1")
    GroupMembersEntity findByGroupIdAndMemberId(String groupId, String memberId);


    @Query(value = "SELECT gm FROM GroupMembersEntity gm WHERE gm.groupId =:groupId AND gm.userId =:memberId")
    GroupMembersEntity findByGroupIdAndMemberIdV2(String groupId, String memberId);
    @Query(value = "SELECt gm FROM GroupMembersEntity gm " +
            "WHERE gm.groupId =:groupId AND gm.status = 1")
    List<GroupMembersEntity> findByGroupIdAndStatus(String groupId);

    @Query(value = "SELECT count(gm) FROM GroupMembersEntity gm WHERE gm.groupId =:groupId and gm.status = 1")
    Long countByGroupId(String groupId);
}
