package org.example.socialbe.dto.comment.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialbe.dto.post.response.PostDetailResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String id;
    private PostDetailResponse.User user;
    private String postId;
    private String content;
    private long likes;
    private List<Reply> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reply {
        private String id;
        private PostDetailResponse.User sender;
        private PostDetailResponse.User receiver;
        private String content;
        private LocalDateTime createdAt;
    }
}
