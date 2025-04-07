package com.apdbank.user.fearture.auth;

import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.auth.dto.AuthResponse;
import com.apdbank.user.fearture.auth.dto.LoginRequest;
import com.apdbank.user.fearture.auth.dto.RefreshTokenRequest;
import com.apdbank.user.fearture.auth.jwt_token.TokenService;
import com.apdbank.user.fearture.user.UserRepository;
import com.apdbank.user.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final TokenService tokenService;
    private final UserRepository userRepository;
;

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findUserByUsername(request.userName())
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User has not been found"
                        )
                );
        // TODO: ensure email is verified
        if (!user.getIsEmailVerified()) {
            if (user.getCreateAt().isBefore(LocalDateTime.now().minusHours(24))) {
                userRepository.delete(user);
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "User has not been verified and has been deleted"
                );
            } else {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "User has not been verified yet"
                );
            }
        }


        // TODO: login with username and password
        Authentication auth = new UsernamePasswordAuthenticationToken(
                request.userName(),
                request.password()
        );

        auth = daoAuthenticationProvider.authenticate(auth);

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return tokenService.createToken(userDetails.getUser(), auth) ;
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {

        Authentication auth = new BearerTokenAuthenticationToken(
                request.refreshToken()
        );

        auth = jwtAuthenticationProvider.authenticate(auth);

        if (auth.getPrincipal() == null) {
            log.error("Authentication principal is null");
        } else {
            log.error("Authentication principal class: {} ", auth.getPrincipal().getClass().getName());
        }

        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized"
            );
        }

        Map<String, Object> claims = jwtAuth.getTokenAttributes();
        String authenticatedUuid = claims.get("uuid").toString();

        User user = userRepository.findByUuid(authenticatedUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User has not been found"
                        )
                );

        return tokenService.createToken(user, auth);

    }
}
