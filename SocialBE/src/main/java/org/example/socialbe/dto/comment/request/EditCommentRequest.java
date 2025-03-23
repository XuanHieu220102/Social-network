package org.example.socialbe.dto.comment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditCommentRequest {
    private String content;
    private String commentId;
}
