package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.request.permission.PermissionCreateRequest;
import com.lamldm.java_api.dto.response.permission.PermissionCreateResponse;
import com.lamldm.java_api.dto.response.permission.PermissionListResponse;
import com.lamldm.java_api.entity.Permission;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    // Create mapping
    Permission toCreatePermissionRequest(PermissionCreateRequest request);

    PermissionCreateResponse toCreatePermissionResponse(Permission permission);

    // List mapping
    List<PermissionListResponse> toPermissionListResponse(List<Permission> permissions);
}
