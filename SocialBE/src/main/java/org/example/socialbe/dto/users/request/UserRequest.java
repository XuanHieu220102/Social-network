package org.example.socialbe.dto.users.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String password;
    private Integer age;
    private String fullName;
    private MultipartFile avatarUrl;
    private String educationLevel;
    private Integer gender;
    private String address;
    private String phoneNumber;
    private String description;
    private LocalDate dateOfBirth;
}
