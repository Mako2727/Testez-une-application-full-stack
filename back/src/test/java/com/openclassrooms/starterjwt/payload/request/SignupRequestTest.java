package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    @Test
    void testGettersAndSetters() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("secret123");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("John", request.getFirstName());
        assertEquals("Doe", request.getLastName());
        assertEquals("secret123", request.getPassword());
    }

    @Test
    void testValidationAnnotations() throws NoSuchFieldException {
        assertTrue(SignupRequest.class.getDeclaredField("email").isAnnotationPresent(javax.validation.constraints.NotBlank.class));
        assertTrue(SignupRequest.class.getDeclaredField("email").isAnnotationPresent(javax.validation.constraints.Email.class));
        assertTrue(SignupRequest.class.getDeclaredField("email").isAnnotationPresent(javax.validation.constraints.Size.class));

        assertTrue(SignupRequest.class.getDeclaredField("firstName").isAnnotationPresent(javax.validation.constraints.NotBlank.class));
        assertTrue(SignupRequest.class.getDeclaredField("firstName").isAnnotationPresent(javax.validation.constraints.Size.class));

        assertTrue(SignupRequest.class.getDeclaredField("lastName").isAnnotationPresent(javax.validation.constraints.NotBlank.class));
        assertTrue(SignupRequest.class.getDeclaredField("lastName").isAnnotationPresent(javax.validation.constraints.Size.class));

        assertTrue(SignupRequest.class.getDeclaredField("password").isAnnotationPresent(javax.validation.constraints.NotBlank.class));
        assertTrue(SignupRequest.class.getDeclaredField("password").isAnnotationPresent(javax.validation.constraints.Size.class));
    }
}