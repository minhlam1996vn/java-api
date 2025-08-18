package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.request.role.RoleCreateRequest;
import com.lamldm.java_api.dto.response.role.RoleCreateResponse;
import com.lamldm.java_api.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRoleCreateRequest(RoleCreateRequest request);

    RoleCreateResponse toRoleCreateResponse(Role role);
}
