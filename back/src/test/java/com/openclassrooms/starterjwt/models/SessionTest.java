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

    @Test
    void testBuilderConstructor() {
        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = new Teacher().setId(2L).setFirstName("Alice").setLastName("Smith");
        User user1 = new User().setId(3L).setEmail("alice@example.com");
        User user2 = new User().setId(4L).setEmail("bob@example.com");

        Session session = Session.builder()
                .id(20L)
                .name("Session Builder Test")
                .date(date)
                .description("Test description")
                .teacher(teacher)
                .users(Arrays.asList(user1, user2))
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(20L, session.getId());
        assertEquals("Session Builder Test", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Test description", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(2, session.getUsers().size());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
void testHashCode() {
    Session session1 = new Session();
    session1.setId(1L);

    Session session2 = new Session();
    session2.setId(1L);

    Session session3 = new Session();
    session3.setId(2L);

    Session sessionNull1 = new Session();
    Session sessionNull2 = new Session();

    // Même id → mêmes hashCode
    assertEquals(session1.hashCode(), session2.hashCode());

    // Id différents → hashCode généralement différents
    assertNotEquals(session1.hashCode(), session3.hashCode());

    // Id null → mêmes hashCode (souvent 0)
    assertEquals(sessionNull1.hashCode(), sessionNull2.hashCode());
}

@Test
void testSessionBuilderToString() {
    Date date = new Date();
    LocalDateTime now = LocalDateTime.now();

    Teacher teacher = new Teacher().setId(1L).setFirstName("John").setLastName("Doe");
    User user1 = new User().setId(1L).setEmail("user1@example.com");
    User user2 = new User().setId(2L).setEmail("user2@example.com");

    // Crée le builder, configure, puis appelle toString dessus
    Session.SessionBuilder builder = Session.builder()
            .id(30L)
            .name("Builder ToString Test")
            .date(date)
            .description("Description test")
            .teacher(teacher)
            .users(Arrays.asList(user1, user2))
            .createdAt(now)
            .updatedAt(now);

    String toString = builder.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("id=30"));
    assertTrue(toString.contains("name=Builder ToString Test"));
    assertTrue(toString.contains("description=Description test"));
    assertTrue(toString.contains("teacher="));
    assertTrue(toString.contains("users="));
    assertTrue(toString.contains("createdAt="));
    assertTrue(toString.contains("updatedAt="));
}
  
}