package org.example.socialbe.dto.users.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenPayload {
    private String userId;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String padding;
}
