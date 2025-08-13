package com.lamldm.java_api.exception;

import com.lamldm.java_api.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Map<String, Object>>> handleException(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("method", request.getMethod());
        body.put("path", request.getRequestURI());
        body.put("type", exception.getClass().getName());
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        List<String> error = Arrays.asList(stringWriter.toString().split("\n"));
        body.put("error", error);

        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.<Map<String, Object>>builder()
                .status(false)
                .message("An unexpected error occurred.")
                .result(body)
                .build();

        return ResponseEntity.internalServerError().body(apiResponse);
    }
}
