package com.lamldm.java_api.controller;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.request.user.UserUpdateRequest;
import com.lamldm.java_api.dto.response.ApiResponse;
import com.lamldm.java_api.dto.response.user.UserCreateResponse;
import com.lamldm.java_api.dto.response.user.UserDetailResponse;
import com.lamldm.java_api.dto.response.user.UserListResponse;
import com.lamldm.java_api.dto.response.user.UserUpdateResponse;
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
    ApiResponse<UserCreateResponse> store(@RequestBody @Valid UserCreateRequest request) {
        UserCreateResponse user = userService.createUser(request);

        return ApiResponse.<UserCreateResponse>builder()
                .message("Created User")
                .result(user)
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserDetailResponse> show(@PathVariable("userId") Integer userId) {
        UserDetailResponse user = userService.getUserById(userId);

        return ApiResponse.<UserDetailResponse>builder()
                .result(user)
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserUpdateResponse> update(
            @PathVariable("userId") Integer userId,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        UserUpdateResponse user = userService.updateUser(userId, request);

        return ApiResponse.<UserUpdateResponse>builder()
                .message("Updated User")
                .result(user)
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> destroy(@PathVariable("userId") Integer userId) {
        userService.deleteUser(userId);

        return ApiResponse.<String>builder()
                .result("Deleted User")
                .build();
    }
}
