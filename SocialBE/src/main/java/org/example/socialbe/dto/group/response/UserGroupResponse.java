package org.example.socialbe.dto.group.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialbe.dto.users.response.UserFilterResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupResponse {
    private long recordSize;
    private List<UserFilterResponse> userGroups;
}
