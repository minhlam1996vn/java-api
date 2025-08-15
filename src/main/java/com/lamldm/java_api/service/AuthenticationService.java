package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.auth.LoginRequest;
import com.lamldm.java_api.dto.request.auth.LogoutRequest;
import com.lamldm.java_api.dto.request.auth.RefreshRequest;
import com.lamldm.java_api.dto.response.auth.LoginResponse;
import com.lamldm.java_api.dto.response.auth.RefreshResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    public LoginResponse login(LoginRequest request) {
        return LoginResponse.builder()
                .accessToken(request.getEmail())
                .refreshToken(request.getPassword())
                .build();
    }

    public RefreshResponse refresh(RefreshRequest request) {
        return RefreshResponse.builder()
                .accessToken(request.getRefreshToken())
                .refreshToken(request.getRefreshToken())
                .build();
    }

    public void logout(LogoutRequest request) {
        log.info("Logout request received");
    }
}
