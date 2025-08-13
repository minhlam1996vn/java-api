package com.lamldm.java_api.controller;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.response.ApiResponse;
import com.lamldm.java_api.dto.response.user.UserCreateResponse;
import com.lamldm.java_api.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping
    ApiResponse<String> index() {
        return ApiResponse.<String>builder()
                .result("OK")
                .build();
    }

    @PostMapping
    ApiResponse<UserCreateResponse> store(@RequestBody @Valid UserCreateRequest request) {
        UserCreateResponse user = userService.createUser(request);

        return ApiResponse.<UserCreateResponse>builder()
                .result(user)
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<String> show(@PathVariable("userId") Integer userId) {
        return ApiResponse.<String>builder()
                .result("Get info user: " + userId)
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<Object> update(@PathVariable("userId") Integer userId, @RequestBody Map<String, Object> request) {
        return ApiResponse.<Object>builder()
                .result(request)
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> destroy(@PathVariable("userId") Integer userId) {
        return ApiResponse.<String>builder()
                .result("Delete user: " + userId)
                .build();
    }
}
