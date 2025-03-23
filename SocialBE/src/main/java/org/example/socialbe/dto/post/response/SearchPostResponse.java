package org.example.socialbe.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostResponse {
    private List<PostDetailResponse> posts;
}
