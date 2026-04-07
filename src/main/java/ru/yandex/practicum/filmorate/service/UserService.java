package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private long nextId = 1L;

    public User createUser(User user) {
        validateUser(user);
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("User created with ID: {}", user.getId());
        return user;
    }

    public User updateUser(User user) {
        Long userId = user.getId();
        if (userId == null || !users.containsKey(userId)) {
            log.warn("Attempt to update non-existent user with ID: {}", userId);
            throw new IllegalArgumentException("Пользователь с ID " + userId + " не найден");
        }
        validateUser(user);
        users.put(userId, user);
        log.info("User updated with ID: {}", userId);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        log.info("Retrieved all users, count: {}", userList.size());
        return userList;
    }

    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            log.warn("User with ID {} not found", id);
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    public void deleteUser(Long id) {
        if (!users.containsKey(id)) {
            log.warn("Attempt to delete non-existent user with ID: {}", id);
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден");
        }
        users.remove(id);
        log.info("User with ID {} deleted", id);
    }

    public void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Validation failed: invalid email");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Validation failed: invalid login");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Validation failed: birthday in future");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("User name set to login: {}", user.getLogin());
        }
    }
}
