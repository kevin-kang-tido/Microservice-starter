package com.apdbank.user.fearture.user.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}
