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
    @Pattern(regexp = "^[A-Z]+$")
    String name;

    @Size(min = 1, max = 100)
    String description;

    @Size(min = 1, max = 50)
    Set<@NotBlank @Pattern(regexp = "^[A-Z0-9_]+$") String> permissions;
}
