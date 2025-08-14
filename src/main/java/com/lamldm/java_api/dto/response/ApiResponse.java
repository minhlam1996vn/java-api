package com.lamldm.java_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data // getter, setter, toString(), equals(), hashCode()
@Builder // ApiResponse.<String>builder().code(200).message("OK").result("Hello").build();
@NoArgsConstructor // new ApiResponse<>()
@AllArgsConstructor // new ApiResponse<>(200, "OK", "Successfully")
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    boolean status = true;
    @Builder.Default
    String message = HttpStatus.OK.getReasonPhrase();
    T result;
}
