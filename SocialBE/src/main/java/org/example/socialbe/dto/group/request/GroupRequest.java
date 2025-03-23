package org.example.socialbe.dto.group.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {
    private String name;
    private String description;
    private MultipartFile profileImage;
    private MultipartFile coverImage;
    private int privacyStatus;
}
