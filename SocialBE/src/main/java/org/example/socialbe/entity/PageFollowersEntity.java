package org.example.socialbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "page_followers")
@Builder
public class PageFollowersEntity {
    @Id
    @GeneratedValue(generator = "mygen")
    @GenericGenerator(name = "mygen", strategy = "org.example.socialbe.util.IdGenerator" )
    @Column(name = "id")
    private String id;

    @Column(name = "page_id")
    private String pageId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "page_role")
    private String role;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
