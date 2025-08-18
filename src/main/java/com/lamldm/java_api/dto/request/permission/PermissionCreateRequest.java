package com.lamldm.java_api.dto.request.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreateRequest {
    @NotBlank
    @Pattern(regexp = "^[A-Z]+$", message = "Name must be uppercase letters only")
    String name;

    @Size(min = 1, max = 100)
    String description;
}
