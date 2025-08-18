package com.lamldm.java_api.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateRequest {
    @NotBlank
    @Pattern(regexp = "^[A-Z]+$", message = "Name must be uppercase letters only")
    String name;

    @Size(min = 1, max = 100)
    String description;

    Set<String> permissions;
}
