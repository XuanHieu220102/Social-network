package org.example.socialbe.dto.page.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagesResponse {
    private String id;
    private Author author;
    private String name;
    private String description;
    private String profileImage;
    private String coverImage;
    private String category;
    private int regime;
    private long totalFollower;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Author {
        private String id;
        private String username;
    }
}
