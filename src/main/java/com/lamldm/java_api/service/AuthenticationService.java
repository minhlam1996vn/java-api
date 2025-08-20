package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.auth.LoginRequest;
import com.lamldm.java_api.dto.request.auth.RefreshRequest;
import com.lamldm.java_api.dto.response.auth.AuthResponse;
import com.lamldm.java_api.dto.response.user.UserResponse;
import com.lamldm.java_api.entity.InvalidatedToken;
import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.exception.AppException;
import com.lamldm.java_api.mapper.AuthMapper;
import com.lamldm.java_api.mapper.UserMapper;
import com.lamldm.java_api.repository.InvalidatedTokenRepository;
import com.lamldm.java_api.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    @NonFinal
    @Value("${jwt.refreshTokenExpirationTime}")
    Long REFRESH_TOKEN_EXPIRATION_TIME;

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    PasswordEncoder passwordEncoder;

    JwtService jwtService;

    AuthMapper authMapper;
    UserMapper userMapper;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .filter(foundUser -> passwordEncoder.matches(request.getPassword(), foundUser.getPassword()))
                .orElseThrow(() -> new AppException("Unauthorized", HttpStatus.UNAUTHORIZED));

        return generateTokens(user);
    }

    public UserResponse getMe() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String email = securityContext.getAuthentication().getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new AppException("Unauthorized", HttpStatus.UNAUTHORIZED));

        return userMapper.toUserResponse(user);
    }

    public AuthResponse refresh(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signJWT = jwtService.verifyRefreshToken(request.getRefreshToken());
        String email = signJWT.getJWTClaimsSet().getSubject();
        String jwtId = signJWT.getJWTClaimsSet().getJWTID();

        if (invalidatedTokenRepository.existsById(jwtId))
            throw new AppException("Token has been invalidated", HttpStatus.UNAUTHORIZED);

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        saveInvalidatedToken(jwtId);

        return generateTokens(user);
    }

    public void logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Jwt jwt) {
            String jwtId = jwt.getId();
            saveInvalidatedToken(jwtId);
        }
    }

    private AuthResponse generateTokens(User user) {
        String jwtId = UUID.randomUUID().toString();
        String accessToken = jwtService.generateAccessToken(user, jwtId);
        String refreshToken = jwtService.generateRefreshToken(user, jwtId);

        return authMapper.toAuthResponse(accessToken, refreshToken);
    }

    private void saveInvalidatedToken(String jwtId) {
        Instant expiryTime = Instant.now().plus(REFRESH_TOKEN_EXPIRATION_TIME, ChronoUnit.SECONDS);

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jwtId)
                .expiryTime(Date.from(expiryTime))
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }
}
