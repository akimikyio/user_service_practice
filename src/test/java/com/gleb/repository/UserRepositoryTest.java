package com.gleb.repository;

import com.gleb.jooq.tables.records.UsersRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
public class UserRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.19");

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Сохранение и поиск пользователя в реальной БД работают корректно")
    void saveAndFindUser() {
        UUID id = UUID.randomUUID();
        String name = "Test Repo User";
        String email = "test_repo@example.com";

        userRepository.save(id, name, email);

        Optional<UsersRecord> foundById = userRepository.findById(id);
        assertTrue(foundById.isPresent(), "Пользователь должен находиться по ID");
        assertEquals(name, foundById.get().getName());

        Optional<UsersRecord> foundByEmail = userRepository.findByEmail(email);
        assertTrue(foundByEmail.isPresent(), "Пользователь должен находиться по Email");
        assertEquals(id, foundByEmail.get().getId());
    }
}
