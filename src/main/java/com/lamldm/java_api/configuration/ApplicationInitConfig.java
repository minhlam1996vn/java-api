package com.lamldm.java_api.configuration;

import com.lamldm.java_api.entity.Role;
import com.lamldm.java_api.entity.User;
import com.lamldm.java_api.enums.RoleUser;
import com.lamldm.java_api.repository.RoleRepository;
import com.lamldm.java_api.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(
            RoleRepository roleRepository,
            UserRepository userRepository
    ) {
        return args -> {
            if (roleRepository.findById("ADMIN").isEmpty()) {
                Role adminRole = Role.builder().name("ADMIN").build();
                roleRepository.save(adminRole);
            }
            if (roleRepository.findById("USER").isEmpty()) {
                Role userRole = Role.builder().name("USER").build();
                roleRepository.save(userRole);
            }

            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User user = User.builder()
                        .name("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("123456789"))
                        .build();

                Set<String> defaultRoles = Set.of(RoleUser.ADMIN.name());
                List<Role> roles = roleRepository.findAllById(defaultRoles);
                user.setRoles(new HashSet<>(roles));

                userRepository.save(user);
            }
        };
    }
}
