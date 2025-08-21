package com.lamldm.java_api.security.handler;

import com.lamldm.java_api.security.util.SecurityResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        SecurityResponseWriter.writeErrorResponse(
                request,
                response,
                HttpStatus.UNAUTHORIZED,
                authException.getMessage()
        );
    }
}
