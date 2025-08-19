package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.permission.PermissionCreateRequest;
import com.lamldm.java_api.dto.response.permission.PermissionResponse;
import com.lamldm.java_api.dto.response.permission.PermissionListResponse;
import com.lamldm.java_api.entity.Permission;
import com.lamldm.java_api.exception.AppException;
import com.lamldm.java_api.mapper.PermissionMapper;
import com.lamldm.java_api.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public List<PermissionListResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();

        return permissions.stream().map(permissionMapper::toPermissionListResponse).toList();
    }

    public PermissionResponse createPermission(PermissionCreateRequest request) {
        Permission permission = permissionMapper.toCreatePermissionRequest(request);
        Permission createdPermission = permissionRepository.save(permission);

        return permissionMapper.toCreatePermissionResponse(createdPermission);
    }

    public void deletePermission(String name) {
        Permission permission = permissionRepository.findById(name).orElseThrow(
                () -> new AppException("Permission not found", HttpStatus.NOT_FOUND));

        permissionRepository.delete(permission);
    }
}
