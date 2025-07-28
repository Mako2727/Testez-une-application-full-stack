package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SpringBootSecurityJwtApplicationMainTest {

    @Test
    void testMainMethod_runsWithoutException() {
        assertDoesNotThrow(() -> SpringBootSecurityJwtApplication.main(new String[]{}));
    }
}