package com.lamldm.java_api.controller;

import com.lamldm.java_api.dto.request.auth.LoginRequest;
import com.lamldm.java_api.dto.request.auth.LogoutRequest;
import com.lamldm.java_api.dto.request.auth.RefreshRequest;
import com.lamldm.java_api.dto.response.ApiResponse;
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
    @PostMapping("/login")
    ApiResponse<?> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.builder()
                .result(request)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<?> refresh(@RequestBody @Valid RefreshRequest request) {
        return ApiResponse.builder()
                .result(request)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<?> logout(@RequestBody @Valid LogoutRequest request) {
        return ApiResponse.builder()
                .result(request)
                .build();
    }
}
