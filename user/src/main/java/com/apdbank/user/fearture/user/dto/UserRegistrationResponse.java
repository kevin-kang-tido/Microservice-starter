package com.apdbank.user.fearture.user.dto;

import java.time.LocalDateTime;

public record UserRegistrationResponse(
        String message,

        Integer code,

        Boolean status,

        LocalDateTime timeStamp,

        String data,

        String token

//        UserDetailResponse user

) {
}
