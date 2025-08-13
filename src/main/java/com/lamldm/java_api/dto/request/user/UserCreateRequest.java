package com.lamldm.java_api.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 100, message = "Name must not exceed 100 characters")
    String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;
}
