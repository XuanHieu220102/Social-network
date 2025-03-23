package org.example.socialbe.dto.post.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharePostRequest {
    private String userId;
    private String postId;
    private String description;
    private int regime;
}
