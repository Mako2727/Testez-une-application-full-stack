package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void testGettersAndSetters() {
        Teacher teacher = new Teacher();

        LocalDateTime now = LocalDateTime.now();

        teacher.setId(1L)
               .setFirstName("John")
               .setLastName("Doe")
               .setCreatedAt(now)
               .setUpdatedAt(now);

        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());

        // Test equals and hashCode based on id
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);

        assertEquals(teacher, teacher2);
        assertEquals(teacher.hashCode(), teacher2.hashCode());

        // Test toString contains main fields
        String toString = teacher.toString();
        assertTrue(toString.contains("Teacher"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("firstName=John"));
        assertTrue(toString.contains("lastName=Doe"));
    }
}