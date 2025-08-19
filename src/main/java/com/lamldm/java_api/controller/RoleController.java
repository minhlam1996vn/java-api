package com.lamldm.java_api.controller;

import com.lamldm.java_api.dto.request.role.RoleCreateRequest;
import com.lamldm.java_api.dto.response.ApiResponse;
import com.lamldm.java_api.dto.response.role.RoleResponse;
import com.lamldm.java_api.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @GetMapping
    ApiResponse<List<RoleResponse>> index() {
        List<RoleResponse> roles = roleService.getAllRoles();

        return ApiResponse.<List<RoleResponse>>builder()
                .result(roles)
                .build();
    }

    @PostMapping
    ApiResponse<RoleResponse> store(@RequestBody @Valid RoleCreateRequest request) {
        RoleResponse role = roleService.createRole(request);

        return ApiResponse.<RoleResponse>builder()
                .result(role)
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> destroy(@PathVariable String role) {
        roleService.deleteRole(role);

        return ApiResponse.<Void>builder()
                .build();
    }
}
