package org.example.socialbe.dto.stories.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SearchStoriesRequest {
    private int page;
    private int size;
    private boolean isValid;
}
