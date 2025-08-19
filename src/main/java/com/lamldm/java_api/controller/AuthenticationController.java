package com.lamldm.java_api.controller;

import com.lamldm.java_api.dto.request.auth.LoginRequest;
import com.lamldm.java_api.dto.request.auth.LogoutRequest;
import com.lamldm.java_api.dto.request.auth.RefreshRequest;
import com.lamldm.java_api.dto.response.ApiResponse;
import com.lamldm.java_api.dto.response.auth.AuthResponse;
import com.lamldm.java_api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse loginResponse = authenticationService.login(request);

        return ApiResponse.<AuthResponse>builder()
                .result(loginResponse)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        AuthResponse refreshResponse = authenticationService.refresh(request);

        return ApiResponse.<AuthResponse>builder()
                .result(refreshResponse)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request) {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .build();
    }
}
