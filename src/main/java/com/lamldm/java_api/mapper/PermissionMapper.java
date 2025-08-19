package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.request.permission.PermissionCreateRequest;
import com.lamldm.java_api.dto.response.permission.PermissionResponse;
import com.lamldm.java_api.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    // Create mapping
    Permission toCreatePermissionRequest(PermissionCreateRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
