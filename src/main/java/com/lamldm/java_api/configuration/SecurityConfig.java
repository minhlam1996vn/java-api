package com.lamldm.java_api.configuration;

import com.lamldm.java_api.enums.RoleUser;
import com.lamldm.java_api.repository.InvalidatedTokenRepository;
import com.lamldm.java_api.security.handler.JwtAccessDeniedHandler;
import com.lamldm.java_api.security.handler.JwtAuthenticationEntryPoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

// https://docs.spring.io/spring-security/reference/servlet/architecture.html

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SecurityConfig {
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.accessTokenKey}")
    String ACCESS_TOKEN_KEY;

    String[] PUBLIC_ENDPOINTS = {
    };

    String[] PUBLIC_GET_ENDPOINTS = {
    };

    String[] PUBLIC_POST_ENDPOINTS = {
            "/auth/login",
            "/auth/refresh",
    };

    String[] PUBLIC_PUT_ENDPOINTS = {
    };

    String[] PUBLIC_DELETE_ENDPOINTS = {
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(
                request -> request
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_PUT_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_DELETE_ENDPOINTS).permitAll()

                        .requestMatchers(
                                "/users",
                                "/roles"
                        )
                        .hasRole(RoleUser.ADMIN.name())

                        .anyRequest().authenticated()
        );

        httpSecurity.oauth2ResourceServer(
                oauth2
                        -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
        );

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // ROLE_ || // default: SCOPE_

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(ACCESS_TOKEN_KEY.getBytes(), "HS512");

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

        OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();

        OAuth2TokenValidator<Jwt> blacklistValidator = jwt -> {
            if (jwt.getExpiresAt() != null && jwt.getExpiresAt().isAfter(java.time.Instant.now())) {
                String jwtId = jwt.getId();
                if (jwtId != null && invalidatedTokenRepository.existsById(jwtId)) {
                    return OAuth2TokenValidatorResult.failure(
                            new org.springframework.security.oauth2.core.OAuth2Error(
                                    "invalid_token",
                                    "Token has been invalidated",
                                    null
                            )
                    );
                }
            }

            return OAuth2TokenValidatorResult.success();
        };

        OAuth2TokenValidator<Jwt> combinedValidator =
                new DelegatingOAuth2TokenValidator<>(defaultValidator, blacklistValidator);

        decoder.setJwtValidator(combinedValidator);

        return decoder;
    }
}
