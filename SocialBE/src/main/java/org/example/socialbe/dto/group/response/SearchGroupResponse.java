package org.example.socialbe.dto.group.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchGroupResponse {
    private long recordSize;
    private List<GroupResponse> groups;
}
