package com.lamldm.java_api.exception;

import com.lamldm.java_api.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Global fallback handler for all uncaught exceptions (Exception.class).
     * - Handles unexpected runtime errors (e.g., NullPointerException, IllegalStateException, etc.).
     * - Returns HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Map<String, Object>>> handleException(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage(), exception);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("method", request.getMethod());
        body.put("path", request.getRequestURI());
        body.put("type", exception.getClass().getName());
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        List<String> errors = Arrays.asList(stringWriter.toString().split("\n"));
        body.put("errors", errors);

        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.<Map<String, Object>>builder()
                .status(false)
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .result(body)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    /**
     * Handler for validation errors when using @Valid on @RequestBody.
     * - Catches MethodArgumentNotValidException thrown by Spring.
     * - Returns HTTP 422 (Unprocessable Entity) with detailed error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleValidationException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);

        Map<String, List<String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        ApiResponse<Map<String, List<String>>> apiResponse = ApiResponse.<Map<String, List<String>>>builder()
                .status(false)
                .message("The given data was invalid.")
                .result(errors)
                .build();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(apiResponse);
    }
}
