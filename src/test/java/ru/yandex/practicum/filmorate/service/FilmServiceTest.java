package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private final FilmService filmService = new FilmService();

    @Test
    void testValidFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Valid description");
        film.setReleaseDate(LocalDate.of(1990, 1, 1));
        film.setDuration(120);

        assertDoesNotThrow(() -> filmService.validateFilm(film));
    }

    @Test
    void testEmptyName() {
        Film film = new Film();
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmService.validateFilm(film));
        assertTrue(exception.getMessage().contains("Название фильма не может быть пустым"));
    }

    @Test
    void testLongDescription() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmService.validateFilm(film));
        assertTrue(exception.getMessage().contains("Описание фильма не может превышать 200 символов"));
    }

    @Test
    void testEarlyReleaseDate() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(90);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmService.validateFilm(film));
        assertTrue(exception.getMessage().contains("Дата релиза не может быть раньше 28 декабря 1895 года"));
    }

    @Test
    void testNegativeDuration() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-10);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmService.validateFilm(film));
        assertTrue(exception.getMessage().contains("Продолжительность фильма должна быть положительным числом"));
    }
}
