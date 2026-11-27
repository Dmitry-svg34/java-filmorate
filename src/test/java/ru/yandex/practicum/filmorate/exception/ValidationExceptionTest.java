package ru.yandex.practicum.filmorate.exception;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ValidationExceptionTest {

    @Test
    void testValidationException_WithMessage() {
        // Given: создаём исключение с сообщением
        String expectedMessage = "Validation failed: name must not be blank";
        ValidationException exception = new ValidationException(expectedMessage);

        // Then: проверяем сообщение исключения
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testValidationException_WithCause() {
        // Given: создаём исключение с причиной
        String message = "Validation error occurred";
        Throwable cause = new IllegalArgumentException("Invalid argument");
        ValidationException exception = new ValidationException(message, cause);

        // Then: проверяем сообщение и причину
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testValidationException_EmptyMessage() {
        // Given: создаём исключение с пустым сообщением
        ValidationException exception = new ValidationException("");

        // Then: проверяем, что сообщение корректно установлено
        assertEquals("", exception.getMessage());
    }
}
