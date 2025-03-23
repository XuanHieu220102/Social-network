package org.example.socialbe.dto.post.request;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String content;
    private int privacy;
    private List<MultipartFile> imageUrls;
    private List<String> tag;
    private List<String> hashtag;
    private String type = "NORMAL";
    private String typeFile;
}
