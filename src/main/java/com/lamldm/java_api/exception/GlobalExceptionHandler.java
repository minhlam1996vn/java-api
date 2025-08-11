package com.lamldm.java_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", exception.getClass().getName());
        body.put("error", exception.getMessage());
        body.put("method", request.getMethod());
        body.put("path", request.getRequestURI());

        return ResponseEntity.badRequest().body(body);
    }
}
