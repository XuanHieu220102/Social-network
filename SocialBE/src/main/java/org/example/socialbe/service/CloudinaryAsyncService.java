package org.example.socialbe.service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CloudinaryAsyncService {

    @Resource
    private Cloudinary cloudinary;

    @Async("uploadTaskExecutor") // Chạy bất đồng bộ trên Thread Pool
    public CompletableFuture<String> uploadImageAsync(MultipartFile file) {
        try {
            String url = uploadImage(file);
            log.info("Uploaded file: {} on thread {}", file.getOriginalFilename(), Thread.currentThread().getName());
            return CompletableFuture.completedFuture(url);
        } catch (IOException e) {
            log.error("Upload failed: {}", file.getOriginalFilename(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private String uploadImage(MultipartFile file) throws IOException {
        var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    private boolean isGifFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("gif");
    }
}