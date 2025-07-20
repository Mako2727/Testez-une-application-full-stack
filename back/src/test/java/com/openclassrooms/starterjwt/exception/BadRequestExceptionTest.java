package com.openclassrooms.starterjwt.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BadRequestExceptionTest {

    @Test
    void testExceptionInstantiation() {
        BadRequestException exception = new BadRequestException();
        assertNotNull(exception);
        assertTrue(exception instanceof RuntimeException);
    }
}