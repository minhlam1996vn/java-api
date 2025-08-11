package com.lamldm.java_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(request);
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok().build();
    }
}

