package ru.yandex.practicum.filmorate.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHandleUserNotFoundException() throws Exception {
        // When: выполняем запрос к несуществующему пользователю
        mockMvc.perform(get("/users/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)))
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }

    @Test
    void testHandleValidationException() throws Exception {
        // Given: некорректные данные пользователя (пустое имя)
        String invalidUserJson = """
            {
                "email": "invalid-email",
                "login": "test",
                "name": "",
                "birthday": "2020-01-01"
            }
            """;

        // When: пытаемся создать пользователя с некорректными данными
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("Name must not be blank")))
                .andExpect(jsonPath("$.message").value(containsString("Email must be a well-formed")))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)))
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }

    @Test
    void testHandleFilmNotFoundException() throws Exception {
        // When: запрашиваем несуществующий фильм
        mockMvc.perform(get("/films/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Film not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)))
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() throws Exception {
        // When: передаём строку вместо числа в параметре ID
        mockMvc.perform(get("/users/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(containsString("Failed to convert value of type 'java.lang.String'")))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)))
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }

    @Test
    void testHandleInternalServerError() throws Exception {
        // When: вызываем endpoint, который выбрасывает исключение
        mockMvc.perform(get("/api/internal-error")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)))
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }

    @Test
    void testHandleConstraintViolationException() throws Exception {
        // Given: данные фильма с некорректной датой (в будущем)
        String invalidFilmJson = """
            {
                "name": "Future Film",
                "description": "A film from the future",
                "releaseDate": "3000-01-01",
                "duration": 120
            }
            """;

        // When: пытаемся создать фильм с некорректной датой
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value(containsString("Release date must be in the past")))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)))
                .andExpect(header().string("Content-Type", containsString("application/json")));
    }
}
