package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.response.auth.AuthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    AuthResponse toAuthResponse(String accessToken, String refreshToken);
}
