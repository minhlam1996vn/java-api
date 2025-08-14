package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.request.user.UserUpdateRequest;
import com.lamldm.java_api.dto.response.user.UserCreateResponse;
import com.lamldm.java_api.dto.response.user.UserDetailResponse;
import com.lamldm.java_api.dto.response.user.UserListResponse;
import com.lamldm.java_api.dto.response.user.UserUpdateResponse;
import com.lamldm.java_api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Create mapping
    User toUserCreateRequest(UserCreateRequest request);

    UserCreateResponse toUserCreateResponse(User user);


    // Update mapping
    void toUserUpdateRequest(@MappingTarget User user, UserUpdateRequest request);

    UserUpdateResponse toUserUpdateResponse(User user);


    // Detail mapping
    UserDetailResponse toUserDetailResponse(User user);

    // List mapping
    List<UserListResponse> toUserListResponse(List<User> users);
}
