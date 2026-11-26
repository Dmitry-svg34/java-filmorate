@Test
void testGetFilmById() throws Exception {
    Film createdFilm = createTestFilm();

    mockMvc.perform(get("/films/{id}", createdFilm.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Film"));
}

@Test
void testCreateFilmWithInvalidReleaseDate() throws Exception {
    Film film = new Film();
    film.setName("Invalid Film");
    film.setDescription("Film with past release date");
    film.setReleaseDate(LocalDate.of(1890, 1, 1)); // раньше 28.12.1895
    film.setDuration(148);

    mockMvc.perform(post("/films")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(film)))
            .andExpect(status().isBadRequest());
}

@Test
void testGetPopularFilmsWithCount() throws Exception {
    for (int i = 0; i < 12; i++) {
        createTestFilm();
    }
    mockMvc.perform(get("/films/popular")
                    .param("count", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(10));
}
