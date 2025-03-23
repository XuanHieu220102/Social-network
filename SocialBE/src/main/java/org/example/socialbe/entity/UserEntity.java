package org.example.socialbe.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @GeneratedValue(generator = "mygen")
    @GenericGenerator(name = "mygen", strategy = "org.example.socialbe.util.IdGenerator")
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "age")
    private Integer age;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "education_level", length = 255)
    private String educationLevel;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone_number", length = 255)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "count_likes")
    private Integer countlikes;

    @Column(name = "count_follows")
    private Integer countFollows;

    @Column(name = "access_token_reset_at")
    private LocalDateTime accessTokenResetAt;

    @Column(name = "failed_login_count")
    private Integer failedLoginCount;

    @Type(JsonType.class)
    @Column(name = "list_images", columnDefinition = "jsonb")
    private List<String> listImages;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
