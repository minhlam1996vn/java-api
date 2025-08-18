package com.lamldm.java_api.dto.response.role;

import com.lamldm.java_api.dto.response.permission.PermissionCreateResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateResponse {
    String name;
    String description;
    Set<PermissionCreateResponse> permissions;
}
