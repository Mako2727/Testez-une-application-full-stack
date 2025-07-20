package com.openclassrooms.starterjwt.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class NotFoundExceptionTest {

    @Test
    void testExceptionInstantiation() {
        NotFoundException exception = new NotFoundException();
        assertNotNull(exception);
        assertTrue(exception instanceof RuntimeException);
    }
}