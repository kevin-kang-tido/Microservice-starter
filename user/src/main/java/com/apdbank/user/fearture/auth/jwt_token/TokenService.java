package com.apdbank.user.fearture.auth.jwt_token;

import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.auth.dto.AuthResponse;
import org.springframework.security.core.Authentication;


public interface TokenService {

    AuthResponse createToken(User user, Authentication authentication);


    String createAccessToken(Authentication authentication);


    String createRefreshToken(Authentication authentication);
}

