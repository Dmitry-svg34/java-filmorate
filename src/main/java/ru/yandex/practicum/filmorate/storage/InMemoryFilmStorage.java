package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFilmStorage {
    private final List<Film> films = new ArrayList<>();
    private Long nextId = 1L;

    public List<Film> getAll() {
        return new ArrayList<>(films);
    }

    public Film add(Film film) {
        film.setId(nextId++);
        films.add(film);
        return film;
    }

    public Film update(Film film) {
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(film.getId())) {
                films.set(i, film);
                return film;
            }
        }
        return null;
    }

    public Film getById(Long id) {
        return films.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
