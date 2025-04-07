package com.apdbank.user.fearture.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(
        @NotNull
        String oldPassword,
        @NotNull
        String newPassword,
        @NotNull
        String confirmedPassword

) {
}
