package com.apdbank.user.fearture.auth.jwt_token;

import com.apdbank.user.domain.User;
import com.apdbank.user.fearture.auth.dto.AuthResponse;
import com.apdbank.user.mapper.UserMapper;
import com.apdbank.user.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;
    private final UserMapper userMapper;

    @Qualifier("refreshJwtEncoder")
    private JwtEncoder refreshJwtEncoder;

    @Qualifier("refreshJwtEncoder")
    @Autowired
    public void setRefreshJwtEncoder(JwtEncoder refreshJwtEncoder) {
        this.refreshJwtEncoder = refreshJwtEncoder;
    }

    @Override
    public AuthResponse createToken(User user, Authentication authentication) {
        final String TOKEN_TYPE = "Bearer";
        return new AuthResponse(
                TOKEN_TYPE,
                createAccessToken(authentication),
                createRefreshToken(authentication),
                userMapper.toUserDetail(user)
        );
    }

    @Override
    public String createAccessToken(Authentication authentication) {

        String scope = "";

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            scope = jwt.getClaimAsString("scope");
        } else {
            scope = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> !authority.startsWith("ROLE_"))
                    .collect(Collectors.joining(" "));
        }

        log.info("Scope: {}", scope);

        String uuid = "";

        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            uuid = customUserDetails.getUser().getUuid();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            uuid = jwt.getClaimAsString("uuid");
        } else {
            log.error("Principal is of type: {}", authentication.getPrincipal().getClass().getName());
            throw new IllegalStateException("Principal is not of type CustomUserDetails or Jwt");
        }

        Instant now = Instant.now();

        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", uuid);

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(authentication.getName())
                .subject("Access Resource")
                .audience(List.of("WEB", "MOBILE"))
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .issuer(authentication.getName())
                .claim("scope", scope)
                .claims(claim -> claim.putAll(claims))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

    }

    @Override
    public String createRefreshToken(Authentication auth) {

        String scope = "";

        if (auth.getPrincipal() instanceof Jwt jwt) {
            scope = jwt.getClaimAsString("scope");
        } else {
            scope = auth.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> !authority.startsWith("ROLE_"))
                    .collect(Collectors.joining(" "));
        }

        String uuid;

        if (auth.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            uuid = customUserDetails.getUser().getUuid();
        } else if (auth.getPrincipal() instanceof Jwt jwt) {
            uuid = jwt.getClaimAsString("uuid");
        } else {
            log.error("Principal is of type: {}", auth.getPrincipal().getClass().getName());
            throw new IllegalStateException("Principal is not of type CustomUserDetails or Jwt");
        }

        Instant now = Instant.now();

        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", uuid);

        JwtClaimsSet refreshJwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Refresh Resource")
                .audience(List.of("WEB", "MOBILE"))
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .issuer(auth.getName())
                .claim("scope", scope)
                .claims(claim -> claim.putAll(claims))
                .build();

        return refreshJwtEncoder.encode(JwtEncoderParameters.from(refreshJwtClaimsSet)).getTokenValue();
    }

}
