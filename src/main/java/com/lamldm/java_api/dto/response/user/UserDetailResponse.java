package com.lamldm.java_api.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailResponse {
    Long id;
    String email;
    String name;
    String password;
}
