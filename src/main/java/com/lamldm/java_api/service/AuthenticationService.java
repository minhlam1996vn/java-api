package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.auth.LoginRequest;
import com.lamldm.java_api.dto.request.auth.LogoutRequest;
import com.lamldm.java_api.dto.request.auth.RefreshRequest;
import com.lamldm.java_api.dto.response.auth.AuthResponse;
import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.exception.AppException;
import com.lamldm.java_api.mapper.AuthMapper;
import com.lamldm.java_api.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    JwtService jwtService;

    AuthMapper authMapper;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .filter(foundUser -> passwordEncoder.matches(request.getPassword(), foundUser.getPassword()))
                .orElseThrow(() -> new AppException("Unauthorized", HttpStatus.UNAUTHORIZED));

        String accessToken = jwtService.generateAccessToken(user, 900);
        String refreshToken = jwtService.generateRefreshToken(user, 604800);

        return authMapper.toAuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(RefreshRequest request) {
        String accessToken = request.getRefreshToken();
        String refreshToken = request.getRefreshToken();

        return authMapper.toAuthResponse(accessToken, refreshToken);
    }

    public void logout(LogoutRequest request) {
        log.info(request.toString(), request);
    }
}
