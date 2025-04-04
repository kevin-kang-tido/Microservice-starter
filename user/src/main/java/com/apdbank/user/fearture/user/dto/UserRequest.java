package com.apdbank.user.fearture.user.dto;

public record UserRequest(
         String username,         // User’s username (unique)
         String avatar,        // URL or Base64 image string for avatar
         String firstName,
         String lastName,
         String phoneNumber,     // Must be unique
         String email,          // Must be unique
         String password,
         String confirmPassword,
         String gender

) {
}
