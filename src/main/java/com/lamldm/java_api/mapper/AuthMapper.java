package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.response.auth.LoginResponse;
import com.lamldm.java_api.dto.response.auth.RefreshResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {
    LoginResponse toLoginResponse(String accessToken, String refreshToken);

    RefreshResponse toRefreshResponse(String accessToken, String refreshToken);
}
