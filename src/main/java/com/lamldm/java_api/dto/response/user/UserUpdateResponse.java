package com.lamldm.java_api.dto.response.user;

import com.lamldm.java_api.dto.response.role.RoleResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateResponse {
    Long id;
    String name;
    String password;
    Set<RoleResponse> roles;
}
