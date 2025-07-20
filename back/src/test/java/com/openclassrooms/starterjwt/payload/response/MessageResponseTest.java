package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void testConstructorAndGetter() {
        MessageResponse response = new MessageResponse("Hello world");
        assertEquals("Hello world", response.getMessage());
    }

    @Test
    void testSetter() {
        MessageResponse response = new MessageResponse("Initial message");
        response.setMessage("Updated message");
        assertEquals("Updated message", response.getMessage());
    }
}
