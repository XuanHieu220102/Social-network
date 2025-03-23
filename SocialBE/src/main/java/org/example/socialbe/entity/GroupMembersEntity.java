package org.example.socialbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "group_members")
public class GroupMembersEntity {
    @Id
    @GeneratedValue(generator = "mygen")
    @GenericGenerator(name = "mygen", strategy = "org.example.socialbe.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role")
    private String role;

    @Column(name = "status")
    private int status;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

}
