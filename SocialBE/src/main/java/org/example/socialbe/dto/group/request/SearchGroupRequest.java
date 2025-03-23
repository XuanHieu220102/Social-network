package org.example.socialbe.dto.group.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialbe.dto.BaseFilterRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchGroupRequest extends BaseFilterRequest {
    private Boolean isOther;
}
