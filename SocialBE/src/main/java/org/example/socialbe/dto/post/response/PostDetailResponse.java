package org.example.socialbe.dto.post.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialbe.dto.comment.response.CommentResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponse {
    private String id;
    private User author;
    private String content;
    private int privacy;
    private List<String> imageUrls;
    private String status;
    private List<User> tags;
    private List<String> hashtag;
    private String type;
    private String typeFile;
    private List<Comment> comments;
    private long countLike;
    private long countComment;
    private long countShare;
    private ShareInfo shareInfo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime updatedAt;



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String id;
        private String username;
        private String avatarUrl;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Comment {
        private String id;
        private String content;
        private int status;
        private List<CommentResponse.Reply> replies;
        private User user;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        private LocalDateTime updatedAt;
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShareInfo {
        private String userId;
        private String username;
        private String avatarUrl;
        private String postId;
        private String description;
        private int regime;
    }
}
