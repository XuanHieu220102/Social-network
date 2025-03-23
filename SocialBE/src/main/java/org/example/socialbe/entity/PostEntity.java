package org.example.socialbe.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(generator = "mygen")
    @GenericGenerator(name = "mygen", strategy = "org.example.socialbe.util.IdGenerator" )
    @Column(name = "post_id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "content")
    private String content;

    @Type(JsonType.class)
    @Column(name = "image_url", columnDefinition = "jsonb")
    private List<String> imageUrls;

    @Column(name = "status")
    private int status;

    @Type(JsonType.class)
    @Column(name = "tag", columnDefinition = "jsonb")
    private List<String> tag;

    @Type(JsonType.class)
    @Column(name = "hashtag", columnDefinition = "jsonb")
    private List<String> hashtag;

    @Column(name = "likes")
    private int likes;

    @Column(name = "comments")
    private int comments;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "type_file")
    private String typeFile;

//    @Column(name = "shares")
//    private int shares;

    @Column(name = "type")
    private String type;

    @Column(name = "privacy")
    private int privacy;

    @Type(JsonType.class)
    @Column(name = "share_info", columnDefinition = "jsonb")
    private Object shareInfo;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
