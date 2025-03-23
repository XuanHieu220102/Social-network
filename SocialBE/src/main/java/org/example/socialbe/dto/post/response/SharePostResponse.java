package org.example.socialbe.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharePostResponse {
    private String userId;
    private String username;
    private String avatarUrl;
    private String postId;
    private String description;
    private int regime;

}
