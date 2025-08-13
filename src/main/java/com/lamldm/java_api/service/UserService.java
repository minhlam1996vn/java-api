package com.lamldm.java_api.service;

import com.lamldm.java_api.dto.request.user.UserCreateRequest;
import com.lamldm.java_api.dto.response.user.UserCreateResponse;
import com.lamldm.java_api.entity.User;
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

    public UserCreateResponse createUser(UserCreateRequest userCreateRequest) {
        User user = new User();
        user.setEmail(userCreateRequest.getEmail());
        user.setName(userCreateRequest.getName());
        user.setPassword(userCreateRequest.getPassword());

        User createdUser = userRepository.save(user);

        return UserCreateResponse.builder()
                .id(createdUser.getId())
                .email(createdUser.getEmail())
                .name(createdUser.getName())
                .build();
    }
}
