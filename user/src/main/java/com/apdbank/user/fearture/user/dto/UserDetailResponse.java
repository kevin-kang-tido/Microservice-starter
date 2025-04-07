package com.apdbank.user.fearture.user.dto;
import java.util.List;

public record UserDetailResponse(

        String uuid,

        String email,

        String firstName,

        String lastName,

        String phoneNumber,

        String username,

        String avatar,

        Boolean isBlocked,

        Boolean isEmailVerified,

        Boolean isAccountNonExpired,

        Boolean isAccountNonLocked,

        Boolean isCredentialsNonExpired,

        List<RoleResponse> roles

) {
}
