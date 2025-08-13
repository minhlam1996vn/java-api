package com.lamldm.java_api.mapper;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.response.user.UserCreateResponse;
import com.lamldm.java_api.dto.response.user.UserDetailResponse;
import com.lamldm.java_api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Map request -> entity
    User toUser(UserCreateRequest userMapper);

    // Map entity -> response
    UserCreateResponse toUserCreateResponse(User user);

    UserDetailResponse toUserDetailResponse(User user);
}
