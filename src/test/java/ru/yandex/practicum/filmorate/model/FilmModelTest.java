package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class FilmModelTest {

    @Test
    void testFilmGettersAndSetters() {
        // Given
        Film film = new Film();

        // When
        Long expectedId = 1L;
        film.setId(expectedId);
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        Set<Long> likes = new HashSet<>();
        likes.add(1L);
        likes.add(2L);
        film.setLikes(likes);

        // Then
        assertEquals(expectedId, film.getId());
        assertEquals("Inception", film.getName());
        assertEquals("A mind-bending thriller", film.getDescription());
        assertEquals(LocalDate.of(2010, 7, 16), film.getReleaseDate());
        assertEquals(148, film.getDuration());
        assertEquals(2, film.getLikes().size());
        assertTrue(film.getLikes().contains(1L));
        assertTrue(film.getLikes().contains(2L));
    }

    @Test
    void testFilmEquals() {
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Inception");
        film2.setDescription("A mind-bending thriller");
        film2.setReleaseDate(LocalDate.of(2010, 7, 16));
        film2.setDuration(148);

        Film film3 = new Film();
        film3.setId(2L);
        film3.setName("Other Film");
        film3.setDescription("Other Description");
        film3.setReleaseDate(LocalDate.of(2020, 1, 1));
        film3.setDuration(120);

        assertEquals(film1, film2);
        assertNotEquals(film3, film1); // Правильный порядок: expected, actual
        assertNotEquals(null, film1); // Правильный порядок: expected, actual
    }

    @Test
    void testFilmHashCode() {
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Inception");
        film2.setDescription("A mind-bending thriller");
        film2.setReleaseDate(LocalDate.of(2010, 7, 16));
        film2.setDuration(148);

        assertEquals(film1.hashCode(), film2.hashCode());
    }

    @Test
    void testFilmToString() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        String toString = film.toString();
        assertTrue(toString.contains("Film"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Inception"));
        assertTrue(toString.contains("duration=148"));
    }

    @Test
    void testFilmConstructorWithAllFields() {
        LocalDate releaseDate = LocalDate.of(2010, 7, 16);
        Film film = new Film();
        film.setId(1L);
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(releaseDate);
        film.setDuration(148);

        assertEquals(1L, film.getId());
        assertEquals("Inception", film.getName());
        assertEquals("A mind-bending thriller", film.getDescription());
        assertEquals(releaseDate, film.getReleaseDate());
        assertEquals(148, film.getDuration());
    }

    @Test
    void testFilmAddLike() {
        Film film = new Film();
        film.setId(1L);

        film.addLike(1L);
        film.addLike(2L);

        assertEquals(2, film.getLikes().size());
        assertTrue(film.getLikes().contains(1L));
        assertTrue(film.getLikes().contains(2L));
    }

    @Test
    void testFilmRemoveLike() {
        Film film = new Film();
        film.setId(1L);

        film.addLike(1L);
        film.addLike(2L);
        film.removeLike(1L);

        assertEquals(1, film.getLikes().size());
        assertFalse(film.getLikes().contains(1L));
        assertTrue(film.getLikes().contains(2L));
    }

    @Test
    void testFilmLikesInitialization() {
        Film film = new Film();
        assertNotNull(film.getLikes());
        assertTrue(film.getLikes().isEmpty());
    }
}
