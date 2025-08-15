package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.auth.LoginRequest;
import com.lamldm.java_api.dto.request.auth.LogoutRequest;
import com.lamldm.java_api.dto.request.auth.RefreshRequest;
import com.lamldm.java_api.dto.response.auth.LoginResponse;
import com.lamldm.java_api.dto.response.auth.RefreshResponse;
import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.exception.AppException;
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

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .filter(foundUser -> passwordEncoder.matches(request.getPassword(), foundUser.getPassword()))
                .orElseThrow(() -> new AppException("Unauthorized", HttpStatus.UNAUTHORIZED));

        String accessToken = jwtService.generateToken(user, 900, true);
        String refreshToken = jwtService.generateToken(user, 604800, false);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RefreshResponse refresh(RefreshRequest request) {
        return RefreshResponse.builder()
                .accessToken(request.getRefreshToken())
                .refreshToken(request.getRefreshToken())
                .build();
    }

    public void logout(LogoutRequest request) {
        log.info(request.toString(), request);
    }
}
