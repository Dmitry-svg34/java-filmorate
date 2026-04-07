package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final List<User> users = new ArrayList<>();
    private int nextId = 1;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        userService.validateUser(user);
        user.setId(nextId++);
        users.add(user);
        log.info("User created: {}", user.getLogin());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        userService.validateUser(user);
        int index = users.indexOf(user);
        if (index != -1) {
            users.set(index, user);
            log.info("User updated: {}", user.getLogin());
            return user;
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Retrieved all users, count: {}", users.size());
        return users;
    }
}
