package org.example.socialbe.dto.page.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeachPageResponse {
    private int recordSize;
    private List<PagesResponse> pages;
}
