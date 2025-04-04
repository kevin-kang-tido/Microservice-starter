package com.apdbank.user.fearture.user.dto;


public record UserResponse(
        String uuid,

        String email,

        String firstName,

        String lastName,

        String phoneNumber,

        String username,

        String avatar,

        Boolean isBlock,

        Boolean isEmailVerified
) {
}
