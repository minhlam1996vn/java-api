package com.lamldm.java_api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamldm.java_api.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * Entry point for handling unauthorized access attempts in Spring Security.
     * - Triggered when an unauthenticated user tries to access a secured resource.
     * - Returns HTTP 401 (Unauthorized) with a structured JSON response.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        HttpStatus responseStatus = HttpStatus.UNAUTHORIZED;

        response.setStatus(responseStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> body = new LinkedHashMap<>();
        body.put("method", request.getMethod());
        body.put("path", request.getRequestURI());
        body.put("error", authException.getMessage());

        ApiResponse<Map<String, String>> apiResponse = ApiResponse.<Map<String, String>>builder()
                .status(false)
                .result(body)
                .message(responseStatus.getReasonPhrase())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
