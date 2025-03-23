package org.example.socialbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SocialBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialBeApplication.class, args);
    }

}
