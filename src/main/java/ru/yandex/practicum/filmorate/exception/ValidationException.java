package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {
    // Конструктор с одним параметром — сообщение
    public ValidationException(String message) {
        super(message);
    }

    // Конструктор с двумя параметрами — сообщение и причина
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
