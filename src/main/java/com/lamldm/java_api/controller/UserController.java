package com.lamldm.java_api.controller;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.request.user.UserUpdateRequest;
import com.lamldm.java_api.dto.response.ApiResponse;
import com.lamldm.java_api.dto.response.user.UserResponse;
import com.lamldm.java_api.dto.response.user.UserListResponse;
import com.lamldm.java_api.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping
    ApiResponse<List<UserListResponse>> index() {
        List<UserListResponse> users = userService.getAllUsers();

        return ApiResponse.<List<UserListResponse>>builder()
                .result(users)
                .build();
    }

    @PostMapping
    ApiResponse<String> store(@RequestBody @Valid UserCreateRequest request) {
        userService.createUser(request);

        return ApiResponse.<String>builder()
                .message("Created")
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> show(@PathVariable("userId") Integer userId) {
        UserResponse user = userService.getUserById(userId);

        return ApiResponse.<UserResponse>builder()
                .result(user)
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<String> update(
            @PathVariable("userId") Integer userId,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        userService.updateUser(userId, request);

        return ApiResponse.<String>builder()
                .message("Updated")
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> destroy(@PathVariable("userId") Integer userId) {
        userService.deleteUser(userId);

        return ApiResponse.<String>builder()
                .result("Deleted")
                .build();
    }
}
