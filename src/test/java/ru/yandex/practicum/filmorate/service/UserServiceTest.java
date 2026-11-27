package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserService userService = new UserService();

    @Test
    void testValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validlogin");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertDoesNotThrow(() -> userService.validateUser(user));
    }

    @Test
    void testInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("validlogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.validateUser(user));
        assertTrue(exception.getMessage().contains("Электронная почта не может быть пустой и должна содержать символ @"));
    }

    @Test
    void testEmptyLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("");
        user.setBirthday(LocalDate.now().minusYears(20));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.validateUser(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым и содержать пробелы"));
    }

    @Test
    void testLoginWithSpaces() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.now().minusYears(20));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.validateUser(user));
        assertTrue(exception.getMessage().contains("Логин не может быть пустым и содержать пробелы"));
    }

    @Test
    void testFutureBirthday() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validlogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.validateUser(user));
        assertTrue(exception.getMessage().contains("Дата рождения не может быть в будущем"));
    }

    @Test
    void testNullNameUsesLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("mylogin");
        user.setBirthday(LocalDate.now().minusYears(25));
        user.setName(null);

        assertDoesNotThrow(() -> userService.validateUser(user));
        assertEquals("mylogin", user.getName());
    }
}
