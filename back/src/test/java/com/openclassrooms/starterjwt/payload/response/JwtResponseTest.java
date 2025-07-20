package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void testConstructorAndGetters() {
        JwtResponse jwtResponse = new JwtResponse(
                "token123",
                1L,
                "user@example.com",
                "John",
                "Doe",
                true
        );

        assertEquals("token123", jwtResponse.getToken());
        assertEquals("Bearer", jwtResponse.getType());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("user@example.com", jwtResponse.getUsername());
        assertEquals("John", jwtResponse.getFirstName());
        assertEquals("Doe", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
    }

    @Test
    void testSetters() {
        JwtResponse jwtResponse = new JwtResponse("token123", 1L, "user", "John", "Doe", false);

        jwtResponse.setToken("newToken");
        jwtResponse.setType("Custom");
        jwtResponse.setId(2L);
        jwtResponse.setUsername("newuser@example.com");
        jwtResponse.setFirstName("Jane");
        jwtResponse.setLastName("Smith");
        jwtResponse.setAdmin(true);

        assertEquals("newToken", jwtResponse.getToken());
        assertEquals("Custom", jwtResponse.getType());
        assertEquals(2L, jwtResponse.getId());
        assertEquals("newuser@example.com", jwtResponse.getUsername());
        assertEquals("Jane", jwtResponse.getFirstName());
        assertEquals("Smith", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
    }
}