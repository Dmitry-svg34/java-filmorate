package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1)); // Исправлено: LocalDate вместо строки

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        // Создаём тестового пользователя
        User createdUser = createTestUser();

        mockMvc.perform(get("/users/{id}", createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User updatedUser = createTestUser();
        updatedUser.setName("Updated Name");

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void testDeleteUser() throws Exception {
        User userToDelete = createTestUser();

        mockMvc.perform(delete("/users/{id}", userToDelete.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllUsers() throws Exception {
        createTestUser(); // Создаём хотя бы одного пользователя

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testCreateUserWithInvalidEmail() throws Exception {
        User user = new User();
        user.setEmail("invalid-email"); // Некорректный email
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1)); // Исправлено: LocalDate

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetNonExistentUser() throws Exception {
        mockMvc.perform(get("/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    private User createTestUser() throws Exception {
        User user = new User();
        user.setEmail("temp@example.com");
        user.setLogin("templogin");
        user.setName("Temp User");
        user.setBirthday(LocalDate.of(1995, 5, 5)); // Исправлено: LocalDate вместо строки

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, User.class);
    }
}
