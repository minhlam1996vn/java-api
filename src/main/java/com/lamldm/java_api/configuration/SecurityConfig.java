package com.lamldm.java_api.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

// https://docs.spring.io/spring-security/reference/servlet/architecture.html

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
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
                        .anyRequest().authenticated()
        );

        httpSecurity.oauth2ResourceServer(
                oauth2
                        -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        return httpSecurity.build();
    }

    /**
     * Creates and registers a PasswordEncoder in the Spring ApplicationContext.
     * Can be injected into other classes via @Autowired or constructor injection.
     * Use BCrypt (strength = 10) for secure one-way password hashing with salt.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(ACCESS_TOKEN_KEY.getBytes(), "HS512");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
