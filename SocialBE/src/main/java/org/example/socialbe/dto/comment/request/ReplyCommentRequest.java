package org.example.socialbe.dto.comment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentRequest {
    private String id;
    private String commentId;
    private String content;
    private String receiverId;
}
