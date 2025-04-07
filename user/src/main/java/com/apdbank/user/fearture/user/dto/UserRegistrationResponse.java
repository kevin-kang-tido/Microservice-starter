package com.apdbank.user.fearture.user.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserRegistrationResponse(
        String message,

        Integer code,

        Boolean status,

        LocalDateTime timeStamp,

        String data,

        String token,

        UserDetailResponse user

) {
}
