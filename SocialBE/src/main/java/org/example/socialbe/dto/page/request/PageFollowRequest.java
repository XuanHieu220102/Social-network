package org.example.socialbe.dto.page.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageFollowRequest {
    private String userId;
    private String pageId;
    private String status;
}
