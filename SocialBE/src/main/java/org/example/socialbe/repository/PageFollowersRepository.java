package org.example.socialbe.repository;

import org.example.socialbe.entity.PageFollowersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageFollowersRepository extends JpaRepository<PageFollowersEntity, String> {
    List<PageFollowersEntity> findByPageId(String pageId);

    @Query("SELECT COUNT(pf) > 0 FROM PageFollowersEntity pf WHERE pf.pageId = :pageId AND pf.userId = :userId AND pf.role = 'ADMIN'")
    boolean isAdmin(@Param("pageId") String pageId, @Param("userId") String userId);

    @Query("SELECT pf FROM PageFollowersEntity pf WHERE pf.userId =:userId AND pf.pageId =:pageId AND pf.status <> 5")
    PageFollowersEntity getUserFollowPage(@Param("userId") String userId, @Param("pageId") String pageId);

    @Query("SELECT pg FROM PageFollowersEntity pg WHERE pg.pageId = :pageId AND pg.role = :role")
    Optional<PageFollowersEntity> getAdminPageFollow(@Param("pageId") String pageId, @Param("role") String role);

    @Query("SELECT pg FROM PageFollowersEntity pg WHERE pg.userId = :userId AND pg.role =:role")
    List<PageFollowersEntity> getMyPage(@Param("userId") String userId, @Param("role") String role);

}
