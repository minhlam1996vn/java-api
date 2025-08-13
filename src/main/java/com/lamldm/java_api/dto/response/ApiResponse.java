package com.lamldm.java_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // getter, setter, toString(), equals(), hashCode()
@Builder // ApiResponse.<String>builder().code(200).message("OK").result("Hello").build();
@NoArgsConstructor // new ApiResponse<>()
@AllArgsConstructor // new ApiResponse<>(200, "OK", "Successfully")
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    boolean status = true;
    String message;
    T result;
}
