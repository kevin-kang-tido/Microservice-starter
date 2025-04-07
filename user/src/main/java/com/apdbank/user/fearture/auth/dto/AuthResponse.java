package com.apdbank.user.fearture.auth.dto;

import com.apdbank.user.fearture.user.dto.UserDetailResponse;

public record AuthResponse(

        String type,

        String accessToken,

        String refreshToken,

        UserDetailResponse user

) {
}