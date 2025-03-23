package org.example.socialbe.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {
    @PostConstruct
    public void init() {
        log.info("AsyncConfig is loaded. @Async should work.");
    }
    @Bean(name = "uploadTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 10 luồng chạy song song
        executor.setMaxPoolSize(10); // Tối đa 10 luồng
        executor.setQueueCapacity(50); // Hàng đợi 50 tác vụ
        executor.setThreadNamePrefix("UploadThread-");
        executor.initialize();
        return executor;
    }
}