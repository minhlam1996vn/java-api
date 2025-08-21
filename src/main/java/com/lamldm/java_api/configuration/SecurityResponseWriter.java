package com.lamldm.java_api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamldm.java_api.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecurityResponseWriter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeErrorResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String errorMessage
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> body = new LinkedHashMap<>();
        body.put("method", request.getMethod());
        body.put("path", request.getRequestURI());
        body.put("error", errorMessage);

        ApiResponse<Map<String, String>> apiResponse = ApiResponse.<Map<String, String>>builder()
                .status(false)
                .result(body)
                .message(status.getReasonPhrase())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
