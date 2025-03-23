package org.example.socialbe.dto.page.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {
    private String name;
    private String description;
//    private MultipartFile coverImage;
//    private MultipartFile profileImage;
    private String category;
    private int regime;
}
