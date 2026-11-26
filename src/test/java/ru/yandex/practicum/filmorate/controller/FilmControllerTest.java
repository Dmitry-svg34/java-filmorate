package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateFilm() throws Exception {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16)); // Исправлено: LocalDate вместо строки
        film.setDuration(148);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Inception"));
    }

    @Test
    void testGetFilmById() throws Exception {
        Film createdFilm = createTestFilm();

        mockMvc.perform(get("/films/{id}", createdFilm.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Inception"));
    }

    @Test
    void testUpdateFilm() throws Exception {
        Film updatedFilm = createTestFilm();
        updatedFilm.setName("Updated Inception");

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedFilm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Inception"));
    }

    @Test
    void testAddLike() throws Exception {
        Film film = createTestFilm();
        User user = createTestUser();

        mockMvc.perform(put("/films/{id}/like/{userId}", film.getId(), user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveLike() throws Exception {
        Film film = createTestFilm();
        User user = createTestUser();

        // Сначала добавляем лайк
        mockMvc.perform(put("/films/{id}/like/{userId}", film.getId(), user.getId()));

        // Затем удаляем
        mockMvc.perform(delete("/films/{id}/like/{userId}", film.getId(), user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPopularFilms() throws Exception {
        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testCreateFilmWithInvalidReleaseDate() throws Exception {
        Film film = new Film();
        film.setName("Invalid Film");
        film.setDescription("Film with past release date");
        film.setReleaseDate(LocalDate.of(1899, 12, 31)); // Дата раньше минимальной
        film.setDuration(148);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPopularFilmsWithCount() throws Exception {
        mockMvc.perform(get("/films/popular")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10));
    }

    private Film createTestFilm() throws Exception {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1)); // Исправлено: LocalDate вместо строки
        film.setDuration(120);

        String response = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, Film.class);
    }

    private User createTestUser() throws Exception {
        User user = new User();
        user.setEmail("test@user.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1)); // Исправлено: LocalDate вместо строки

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, User.class);
    }
}
