package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        Film added = filmService.addFilm(film);
        log.info("Film added: id={}, name={}", added.getId(), added.getName());
        return added;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film updated = filmService.updateFilm(film);
        log.info("Film updated: id={}", updated.getId());
        return updated;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        log.info("Retrieved all films, count: {}", films.size());
        return films;
    }
}
