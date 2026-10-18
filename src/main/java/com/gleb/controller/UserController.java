package com.gleb.controller;

import com.gleb.dto.UserCreateRequest;
import com.gleb.dto.UserResponse;
import com.gleb.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public UserResponse getByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
}
