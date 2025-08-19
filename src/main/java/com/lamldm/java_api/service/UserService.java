package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.request.user.UserUpdateRequest;
import com.lamldm.java_api.dto.response.user.UserCreateResponse;
import com.lamldm.java_api.dto.response.user.UserDetailResponse;
import com.lamldm.java_api.dto.response.user.UserListResponse;
import com.lamldm.java_api.dto.response.user.UserUpdateResponse;
import com.lamldm.java_api.entity.Role;
import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.enums.RoleUser;
import com.lamldm.java_api.exception.AppException;
import com.lamldm.java_api.mapper.UserMapper;
import com.lamldm.java_api.repository.RoleRepository;
import com.lamldm.java_api.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    private User getUserOrThrow(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    public List<UserListResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toUserListResponse(users);
    }

    public UserCreateResponse createUser(UserCreateRequest request) {
        User user = userMapper.toUserCreateRequest(request);
        String encodePassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodePassword);

        Set<String> defaultRoles = Set.of(RoleUser.USER.name());
        List<Role> roles = roleRepository.findAllById(defaultRoles);
        user.setRoles(new HashSet<>(roles));

        User createdUser = userRepository.save(user);

        return userMapper.toUserCreateResponse(createdUser);
    }

    public UserDetailResponse getUserById(Integer userId) {
        User user = getUserOrThrow(userId);

        return userMapper.toUserDetailResponse(user);
    }

    public UserUpdateResponse updateUser(Integer userId, UserUpdateRequest request) {
        User user = getUserOrThrow(userId);

        userMapper.toUserUpdateRequest(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            List<Role> roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toUserUpdateResponse(updatedUser);
    }

    public void deleteUser(Integer userId) {
        User user = getUserOrThrow(userId);

        userRepository.delete(user);
    }
}
