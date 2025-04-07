package com.apdbank.user.fearture.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequest(

        @NotBlank(message = "Username is required")
        String userName,

        @NotBlank(message = "Password is required")
//        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password

) {
}
