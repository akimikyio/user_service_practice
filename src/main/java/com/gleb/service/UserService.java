package com.gleb.service;

import com.gleb.dto.UserCreateRequest;
import com.gleb.dto.UserResponse;
import com.gleb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserCreateRequest request) {
        UUID newId = UUID.randomUUID();

        userRepository.save(newId, request.name(), request.email());

        return new UserResponse(newId, request.name(), request.email());
    }

    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(record -> new UserResponse(record.getId(), record.getName(), record.getEmail()))
                .orElseThrow(() -> new RuntimeException("Пользователь с email " + email + " не найден"));
    }
}
