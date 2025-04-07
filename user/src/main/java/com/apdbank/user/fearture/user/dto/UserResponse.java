package com.apdbank.user.fearture.user.dto;


public record UserResponse(
        String uuid,

        String firstName,

        String lastName,

        String  username,

        String  password,

        String confirmPassword,

        String phoneNumber,

        String email,

        String avatar,

        Boolean isEmailVerified
) {
}
