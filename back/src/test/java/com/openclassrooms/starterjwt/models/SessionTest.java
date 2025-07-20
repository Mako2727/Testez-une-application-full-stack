package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    void testGettersAndSetters() {
        Session session = new Session();

        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = new Teacher().setId(1L).setFirstName("John").setLastName("Doe");
        User user1 = new User().setId(1L).setEmail("user1@example.com");
        User user2 = new User().setId(2L).setEmail("user2@example.com");

        session.setId(10L);
        session.setName("Session Test");
        session.setDate(date);
        session.setDescription("Description de la session");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        assertEquals(10L, session.getId());
        assertEquals("Session Test", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Description de la session", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(2, session.getUsers().size());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());

        // Test equals and hashCode with same id
        Session session2 = new Session();
        session2.setId(10L);

        assertEquals(session, session2);
        assertEquals(session.hashCode(), session2.hashCode());

        // Test toString contains key info
        String toString = session.toString();
        assertTrue(toString.contains("Session"));
        assertTrue(toString.contains("id=10"));
    }
}