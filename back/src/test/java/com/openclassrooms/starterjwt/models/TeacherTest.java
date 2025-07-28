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
@Test
void testHashCode() {
    Teacher teacher1 = new Teacher();
    teacher1.setId(1L);

    Teacher teacher2 = new Teacher();
    teacher2.setId(1L);

    Teacher teacher3 = new Teacher();
    teacher3.setId(2L);

    Teacher teacherNull1 = new Teacher();
    Teacher teacherNull2 = new Teacher();

    // Même id → mêmes hashCode
    assertEquals(teacher1.hashCode(), teacher2.hashCode());

    // Id différents → hashCode différents (en général)
    assertNotEquals(teacher1.hashCode(), teacher3.hashCode());

    // Id null → mêmes hashCode (souvent 0)
    assertEquals(teacherNull1.hashCode(), teacherNull2.hashCode());
}

@Test
void testEquals() {
    Teacher teacher1 = new Teacher();
    teacher1.setId(1L);

    Teacher teacher2 = new Teacher();
    teacher2.setId(1L);

    Teacher teacher3 = new Teacher();
    teacher3.setId(2L);

    Teacher teacherNull = new Teacher();

    // Réflexivité
    assertEquals(teacher1, teacher1);

    // Comparaison avec null
    assertNotEquals(teacher1, null);

    // Comparaison avec un objet d'une autre classe
    assertNotEquals(teacher1, "some string");

    // Même id → égaux
    assertEquals(teacher1, teacher2);

    // Id différents → pas égaux
    assertNotEquals(teacher1, teacher3);

    // Id null, 2 objets distincts avec id null → égaux selon Lombok (si @EqualsAndHashCode(of="id"))
    Teacher teacherNull2 = new Teacher();
    assertEquals(teacherNull, teacherNull2);

    // Id null vs id non null → pas égaux
    assertNotEquals(teacher1, teacherNull);
}

@Test
void testTeacherBuilderToString() {
    LocalDateTime now = LocalDateTime.now();

    Teacher.TeacherBuilder builder = Teacher.builder()
        .id(42L)
        .firstName("Alice")
        .lastName("Smith")
        .createdAt(now)
        .updatedAt(now);

    String toString = builder.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("id=42"));
    assertTrue(toString.contains("firstName=Alice"));
    assertTrue(toString.contains("lastName=Smith"));
    assertTrue(toString.contains("createdAt="));
    assertTrue(toString.contains("updatedAt="));
}
    
}