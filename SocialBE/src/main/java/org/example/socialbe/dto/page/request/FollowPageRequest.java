package org.example.socialbe.dto.page.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowPageRequest {
    private String pageId;
    private Integer status;
}
