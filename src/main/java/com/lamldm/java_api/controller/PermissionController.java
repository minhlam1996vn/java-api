package com.lamldm.java_api.controller;

import com.lamldm.java_api.dto.request.permission.PermissionCreateRequest;
import com.lamldm.java_api.dto.response.ApiResponse;
import com.lamldm.java_api.dto.response.permission.PermissionResponse;
import com.lamldm.java_api.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    ApiResponse<List<PermissionResponse>> index() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();

        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissions)
                .build();
    }

    @PostMapping
    ApiResponse<PermissionResponse> store(@RequestBody @Valid PermissionCreateRequest request) {
        PermissionResponse permission = permissionService.createPermission(request);

        return ApiResponse.<PermissionResponse>builder()
                .message("Created permission")
                .result(permission)
                .build();
    }

    @DeleteMapping("/{permissionName}")
    ApiResponse<Void> destroy(@PathVariable("permissionName") String permissionName) {
        permissionService.deletePermission(permissionName);

        return ApiResponse.<Void>builder()
                .message("Deleted permission")
                .build();
    }
}
