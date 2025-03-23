package org.example.socialbe.dto.group.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusGroupMemberRequest {
    private String groupId;
    private String memberId;
    private int status;
}
