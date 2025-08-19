package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.request.user.UserUpdateRequest;
import com.lamldm.java_api.dto.response.user.UserResponse;
import com.lamldm.java_api.dto.response.user.UserListResponse;
import com.lamldm.java_api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Create mapping
    User toUserCreateRequest(UserCreateRequest request);

    // Update mapping
    @Mapping(target = "roles", ignore = true)
    void toUserUpdateRequest(@MappingTarget User user, UserUpdateRequest request);

    // Detail mapping
    UserResponse toUserDetailResponse(User user);

    // List mapping
    List<UserListResponse> toUserListResponse(List<User> users);
}
