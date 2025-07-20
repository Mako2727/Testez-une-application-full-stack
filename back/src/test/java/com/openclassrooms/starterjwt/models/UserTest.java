package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L)
            .setEmail("user@example.com")
            .setFirstName("John")
            .setLastName("Doe")
            .setPassword("secret123")
            .setAdmin(true)
            .setCreatedAt(now)
            .setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("secret123", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());

        // Test equals and hashCode based on id
        User user2 = new User();
        user2.setId(1L);

        assertEquals(user, user2);
        assertEquals(user.hashCode(), user2.hashCode());

        // Test toString contains main fields
        String toString = user.toString();
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("email=user@example.com"));
        assertTrue(toString.contains("firstName=John"));
        assertTrue(toString.contains("lastName=Doe"));
    }
}