package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternalErrorController {

    @GetMapping("/api/internal-error")
    public void internalError() {
        throw new RuntimeException("Internal error triggered for testing");
    }
}
