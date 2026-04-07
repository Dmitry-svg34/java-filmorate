package ru.yandex.practicum.filmorate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ValidationExceptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testValidationException_MissingNameField() throws Exception {
        // Given: некорректные данные пользователя (отсутствует поле name)
        String invalidUserJson = """
            {
                "email": "invalid-email",
                "login": "test"
            }
            """;

        // When: пытаемся создать пользователя с неполными данными
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                // Then: проверяем ответ валидации
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("name")))
                .andExpect(jsonPath("$.message").value(containsString("must not be null")))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)))
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }

    @Test
    void testValidationException_InvalidEmailFormat() throws Exception {
        // Given: некорректный email (не соответствует формату)
        String invalidUserJson = """
            {
                "email": "not-an-email",
                "login": "testuser",
                "name": "Test User",
                "birthday": "2000-01-01"
            }
            """;

        // When: пытаемся создать пользователя с некорректным email
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                // Then: проверяем обработку ошибки формата email
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("email")))
                .andExpect(jsonPath("$.message").value(containsString("must be a well-formed email address")))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)))
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }
}
