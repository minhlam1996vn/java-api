package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.role.RoleCreateRequest;
import com.lamldm.java_api.dto.response.role.RoleResponse;
import com.lamldm.java_api.entity.Permission;
import com.lamldm.java_api.entity.Role;
import com.lamldm.java_api.mapper.RoleMapper;
import com.lamldm.java_api.repository.PermissionRepository;
import com.lamldm.java_api.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;

    RoleMapper roleMapper;

    public RoleResponse createRole(RoleCreateRequest request) {
        Role role = roleMapper.toRoleCreateRequest(request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));
        Role createRole = roleRepository.save(role);

        return roleMapper.toRoleCreateResponse(createRole);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleCreateResponse)
                .collect(Collectors.toList());
    }

    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
