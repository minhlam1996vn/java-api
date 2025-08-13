package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.response.user.UserCreateResponse;
import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.mapper.UserMapper;
import com.lamldm.java_api.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserCreateResponse createUser(UserCreateRequest request) {
        User user = userMapper.toUser(request);
        User createdUser = userRepository.save(user);

        return userMapper.toUserCreateResponse(createdUser);
    }
}
