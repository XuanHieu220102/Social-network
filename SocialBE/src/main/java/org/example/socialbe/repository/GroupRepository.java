package org.example.socialbe.repository;

import org.example.socialbe.entity.GroupsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupsEntity, String> {
    @Query(value = "SELECT g FROM GroupsEntity g WHERE g.name =:name AND g.deleted = false ")
    GroupsEntity findByNameAndDeletedFalse(String name);

    @Query(value = "SELECT g.* FROM groups g " +
            "JOIN group_members gm ON g.id = gm.group_id " +
            "WHERE gm.user_id = :userId " +
            "AND gm.role = 'member' " +
            "AND gm.status = 1 " +
            "AND g.id NOT IN ( " +
            "    SELECT gm2.group_id FROM group_members gm2 " +
            "    WHERE gm2.user_id = :userId AND gm2.role = 'admin' " +
            ")", nativeQuery = true)
    List<GroupsEntity> findGroupsUserJoinedAsMemberExcludingAdmins(@Param("userId") String userId);

    @Query(value = "SELECT g.* FROM groups g " +
            "WHERE g.created_by != :userId " +
            "AND g.id NOT IN ( " +
            "    SELECT gm.group_id FROM group_members gm " +
            "    WHERE gm.user_id = :userId " +
            ")", nativeQuery = true)
    List<GroupsEntity> findGroupsUserNotJoined(@Param("userId") String userId);

}
