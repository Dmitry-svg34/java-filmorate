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
        String invalidUserJson = "{\n" +
                "  \"email\": \"invalid-email\",\n" +
                "  \"login\": \"test\"\n" +
                "}";

        // When: пытаемся создать пользователя с неполными данными
        // Then: проверяем ответ валидации
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
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
        String invalidUserJson = "{\n" +
                "  \"email\": \"not-an-email\",\n" +
                "  \"login\": \"testuser\",\n" +
                "  \"name\": \"Test User\",\n" +
                "  \"birthday\": \"2000-01-01\"\n" +
                "}";

        // When: пытаемся создать пользователя с некорректным email
        // Then: проверяем обработку ошибки формата email
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
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
