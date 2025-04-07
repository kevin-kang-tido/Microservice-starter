package com.apdbank.user.security;

import com.apdbank.user.utils.KeyUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity  // enable to make config
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final KeyUtil keyUtil;

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(@Qualifier("refreshJwtDecoder") JwtDecoder refreshJwtDecoder) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(refreshJwtDecoder);
        return provider;
    }
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;

    }

    @Bean
    AuthenticationManager authenticationManager(){
        return  new ProviderManager(List.of(daoAuthenticationProvider()));
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(HttpMethod.GET,"/api/v1/users/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"/api/v1/users/**").permitAll()
                            .requestMatchers(HttpMethod.PUT,"/api/v1/users/disable/**").hasAnyAuthority("SCOPE_admin:read","SCOPE_admin:write","SCOPE_user:read")
                            .requestMatchers(HttpMethod.PUT,"/api/v1/users/enable/**").hasAnyAuthority("SCOPE_admin:read","SCOPE_admin:write")
                            .requestMatchers(HttpMethod.PUT,"/api/v1/users/resetPassword/**").permitAll()
                            .requestMatchers(
                                    "/microservice-api-docs/**", // OpenAPI docs
                                    "/v3/api-docs/**",          // Default API docs path
                                    "/swagger-ui/**",           // Default Swagger UI assets
                                    "/microservice-api-ui.html"  // Custom Swagger UI
                            ).permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                            .requestMatchers("/error").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/error").permitAll()// ✅ Allow access to error handling
                            .anyRequest().authenticated();
                });
        // Security mechanism
        http.oauth2ResourceServer(jwt -> jwt
                .jwt(Customizer.withDefaults()));

        // Disable
        http.csrf(AbstractHttpConfigurer::disable);

        // Change to stateless
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    };

    @Primary
    @Bean
    JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = new RSAKey.Builder(keyUtil.getAccessTokenPublicKey())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector
                .select(jwkSet);
    }

    @Primary
    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Primary
    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(keyUtil.getAccessTokenPublicKey())
                .build();
    }

    // JWT REFRESH TOKEN =====================================

    @Bean("refreshJwkSource")
    JWKSource<SecurityContext> refreshJwkSource() {
        RSAKey rsaKey = new RSAKey.Builder(keyUtil.getRefreshTokenPublicKey())
                .privateKey(keyUtil.getRefreshTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector
                .select(jwkSet);
    }

    @Bean("refreshJwtEncoder")
    JwtEncoder refreshJwtEncoder(@Qualifier("refreshJwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean("refreshJwtDecoder")
    JwtDecoder refreshJwtDecoder() throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(keyUtil.getRefreshTokenPublicKey())
                .build();
    }
}
