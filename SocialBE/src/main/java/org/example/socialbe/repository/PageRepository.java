package org.example.socialbe.repository;

import org.example.socialbe.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, String> {
    PageEntity findByNameAndDeletedFalse(String name);

    @Query(value = "SELECT p FROM PageEntity p " +
            "JOIN PageFollowersEntity pf ON pf.pageId = p.id " +
            "WHERE pf.userId <> :userId AND pf.role = :role " +
            "AND NOT EXISTS (SELECT 1 FROM PageFollowersEntity pf2 " +
            "                WHERE pf2.pageId = p.id " +
            "                AND pf2.userId = :userId " +
            "                AND pf2.role = 'MEMBER')")
    List<PageEntity> getPageByUserIdAndRole(@Param("userId") String userId, @Param("role") String role);

    @Query(value = "SELECT p FROM PageEntity p " +
            "JOIN PageFollowersEntity pf ON pf.pageId = p.id " +
            "WHERE pf.userId = :userId AND pf.role = :role")
    List<PageEntity> getPageUserFollowing(@Param("userId") String userId, @Param("role") String role);

    @Query(value = "SELECT count(pf) FROM PageFollowersEntity pf WHERE pf.pageId =:pageId AND pf.role = 'MEMBER'")
    long countUserFollowPage(@Param("pageId") String pageId);

    @Query(value = "SELECT p, COUNT(pf) as memberCount FROM PageEntity p " +
            "LEFT JOIN PageFollowersEntity pf ON pf.pageId = p.id AND pf.role = 'MEMBER' " +
            "WHERE NOT EXISTS ( " +
            "    SELECT 1 FROM PageFollowersEntity pf2 " +
            "    WHERE pf2.pageId = p.id " +
            "    AND pf2.userId = :userId " +
            ") " +
            "GROUP BY p " +
            "ORDER BY COUNT(pf) DESC " +
            "LIMIT 12")
    List<Object[]> getPopularPage(@Param("userId") String userId);



}
