package org.example.socialbe.dto.stories.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoriesRequest {
    private String content;
    private MultipartFile videoUrl;
    private String backgroundColor;
    private String type;
    private LocalDateTime expiresAt;
}
