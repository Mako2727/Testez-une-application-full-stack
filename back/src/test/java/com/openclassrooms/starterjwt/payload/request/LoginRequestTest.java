package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testGettersAndSetters() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("secret");

        assertEquals("user@example.com", request.getEmail());
        assertEquals("secret", request.getPassword());
    }

    @Test
    void testNotBlankConstraints() throws NoSuchFieldException {
        // Vérifie que les annotations @NotBlank sont bien présentes sur les champs
        assertTrue(LoginRequest.class.getDeclaredField("email").isAnnotationPresent(javax.validation.constraints.NotBlank.class));
        assertTrue(LoginRequest.class.getDeclaredField("password").isAnnotationPresent(javax.validation.constraints.NotBlank.class));
    }
}