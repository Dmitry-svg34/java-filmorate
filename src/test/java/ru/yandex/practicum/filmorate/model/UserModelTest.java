package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void testUserGettersAndSetters() {
        // Given
        User user = new User();

        // When
        Long expectedId = 1L;
        user.setId(expectedId);
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // Then
        assertEquals(expectedId, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testlogin", user.getLogin());
        assertEquals("Test User", user.getName());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthday());
    }

    @Test
    void testUserEquals() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("email@example.com");
        user1.setLogin("login");
        user1.setName("Name");
        user1.setBirthday(LocalDate.now());

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("email@example.com");
        user2.setLogin("login");
        user2.setName("Name");
        user2.setBirthday(LocalDate.now());

        User user3 = new User();
        user3.setId(2L);
        user3.setEmail("other@example.com");
        user3.setLogin("otherlogin");
        user3.setName("Other");
        user3.setBirthday(LocalDate.of(1995, 5, 5));

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(null, user1);
    }

    @Test
    void testUserHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("email@example.com");
        user1.setLogin("login");
        user1.setName("Name");
        user1.setBirthday(LocalDate.now());

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("email@example.com");
        user2.setLogin("login");
        user2.setName("Name");
        user2.setBirthday(LocalDate.now());

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testUserToString() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        String toString = user.toString();
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("email=test@example.com"));
    }

    @Test
    void testUserConstructorWithAllFields() {
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(birthday);

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("testlogin", user.getLogin());
        assertEquals("Test User", user.getName());
        assertEquals(birthday, user.getBirthday());
    }
}
