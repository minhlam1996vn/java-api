package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.request.user.UserUpdateRequest;
import com.lamldm.java_api.dto.response.user.UserCreateResponse;
import com.lamldm.java_api.dto.response.user.UserDetailResponse;
import com.lamldm.java_api.dto.response.user.UserListResponse;
import com.lamldm.java_api.dto.response.user.UserUpdateResponse;
import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.mapper.UserMapper;
import com.lamldm.java_api.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public List<UserListResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toUserListResponse(users);
    }

    public UserCreateResponse createUser(UserCreateRequest request) {
        User user = userMapper.toUserCreateRequest(request);
        User createdUser = userRepository.save(user);

        return userMapper.toUserCreateResponse(createdUser);
    }

    public UserDetailResponse getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserDetailResponse(user);
    }

    public UserUpdateResponse updateUser(Integer userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        userMapper.toUserUpdateRequest(user, request);
        User updatedUser = userRepository.save(user);

        return userMapper.toUserUpdateResponse(updatedUser);
    }

    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));

        userRepository.delete(user);
    }
}
