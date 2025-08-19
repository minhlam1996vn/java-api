package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.request.permission.PermissionCreateRequest;
import com.lamldm.java_api.dto.response.permission.PermissionResponse;
import com.lamldm.java_api.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    // Mapping request
    Permission toCreatePermissionRequest(PermissionCreateRequest request);

    // Mapping response
    PermissionResponse toPermissionResponse(Permission permission);
}
