package com.apdbank.user.fearture.auth;

import com.apdbank.user.fearture.auth.dto.AuthResponse;
import com.apdbank.user.fearture.auth.dto.LoginRequest;
import com.apdbank.user.fearture.auth.dto.RefreshTokenRequest;


public interface AuthService {


    AuthResponse login(LoginRequest request);


    AuthResponse refresh(RefreshTokenRequest request);
}

