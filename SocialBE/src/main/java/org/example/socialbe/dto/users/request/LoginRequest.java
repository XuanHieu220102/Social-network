package org.example.socialbe.dto.users.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
