package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;
    private final List<Film> films = new ArrayList<>();
    private Long nextId = 1L;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        filmService.validateFilm(film);
        film.setId(nextId++);
        films.add(film);
        log.info("Film added: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        filmService.validateFilm(film);
        int index = films.indexOf(film);
        if (index != -1) {
            films.set(index, film);
            log.info("Film updated: {}", film.getName());
            return film;
        } else {
            throw new IllegalArgumentException("Фильм не найден");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Retrieved all films, count: {}", films.size());
        return films;
    }
}
