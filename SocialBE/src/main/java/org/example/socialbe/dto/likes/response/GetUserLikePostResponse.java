package org.example.socialbe.dto.likes.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class GetUserLikePostResponse {
    private int recordSize;
    private List<UserLikePost> userLikePosts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLikePost {
        private String id;
        private String username;
        private String avatarUrl;
    }

}
