package org.example.socialbe.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.dto.ErrorResponse;
import org.example.socialbe.dto.UploadVideoRequest;
import org.example.socialbe.service.CloudinaryService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {
    @Resource
    private CloudinaryService cloudinaryService;

    @PostMapping
    public ErrorResponse upload(@ModelAttribute UploadVideoRequest request) throws IOException {
        log.info("Upload request: {}", request);
        return ErrorResponse.success(cloudinaryService.uploadMedia(request.getFile()));
    }
}
