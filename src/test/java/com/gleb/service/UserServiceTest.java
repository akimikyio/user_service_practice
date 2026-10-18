package com.gleb.service;

import com.gleb.dto.UserCreateRequest;
import com.gleb.dto.UserResponse;
import com.gleb.jooq.tables.records.UsersRecord;
import com.gleb.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Создание пользователя: должен сгенерировать UUID и вызвать сохранение в репозиторий")
    void createUser_ShouldSaveAndReturnResponse() {
        UserCreateRequest request = new UserCreateRequest("jeff", "jeff@example.com");

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertNotNull(response.id(), "ID пользователя должен быть сгенерирован");
        assertEquals("jeff", response.name());
        assertEquals("jeff@example.com", response.email());

        verify(userRepository, times(1)).save(
                eq(response.id()),
                eq("jeff"),
                eq("jeff@example.com")
        );
    }

    @Test
    @DisplayName("Поиск по email: возвращает пользователя, если он найден в БД")
    void getUserByEmail_WhenUserExists_ShouldReturnUserResponse() {
        String email = "jeff@example.com";
        UUID userId = UUID.randomUUID();

        UsersRecord fakeRecord = new UsersRecord();
        fakeRecord.setId(userId);
        fakeRecord.setName("jeff");
        fakeRecord.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(fakeRecord));

        UserResponse result = userService.getUserByEmail(email);

        assertEquals(userId, result.id());
        assertEquals("jeff", result.name());
        assertEquals(email, result.email());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Поиск по email: выбрасывает исключение, если пользователь не найден")
    void getUserByEmail_WhenUserNotFound_ShouldThrowException() {
        String email = "unknown@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserByEmail(email);
        });

        assertEquals("Пользователь с email unknown@example.com не найден", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

}
