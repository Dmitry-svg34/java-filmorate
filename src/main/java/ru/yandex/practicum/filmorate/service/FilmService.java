package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    private final InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Validation failed: film name cannot be empty");
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Validation failed: film description too long");
            throw new ValidationException("Описание фильма не может превышать 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(earliestReleaseDate)) {
            log.warn("Validation failed: film release date too early");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            log.warn("Validation failed: film duration must be positive");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        Film added = filmStorage.add(film);
        log.info("Film added: id={}, name={}", added.getId(), added.getName());
        return added;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null || filmStorage.getById(film.getId()) == null) {
            log.warn("Update failed: film with id={} not found", film.getId());
            throw new NotFoundException("Film not found");
        }
        validateFilm(film);
        Film updated = filmStorage.update(film);
        log.info("Film updated: id={}", updated.getId());
        return updated;
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        return film;
    }

    public List<Film> getPopularFilms(@Positive int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(@Positive Long id, @Positive Long userId) {
        getFilmById(id).addLike(userId);
    }

    public void removeLike(@Positive Long id, @Positive Long userId) {
        getFilmById(id).removeLike(userId);
    }
}
