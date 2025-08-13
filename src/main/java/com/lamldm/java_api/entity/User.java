package com.lamldm.java_api.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, unique = true)
    String email;

    LocalDateTime emailVerifiedAt;

    @Column(nullable = false)
    String password;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

    LocalDateTime deletedAt;
}
