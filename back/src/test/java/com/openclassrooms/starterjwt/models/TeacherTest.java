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

        
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);

        assertEquals(teacher, teacher2);
        assertEquals(teacher.hashCode(), teacher2.hashCode());

        
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

    
    assertEquals(teacher1.hashCode(), teacher2.hashCode());

   
    assertNotEquals(teacher1.hashCode(), teacher3.hashCode());

    
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

    
    assertEquals(teacher1, teacher1);

    
    assertNotEquals(teacher1, null);

    
    assertNotEquals(teacher1, "some string");

    
    assertEquals(teacher1, teacher2);

    
    assertNotEquals(teacher1, teacher3);

    
    Teacher teacherNull2 = new Teacher();
    assertEquals(teacherNull, teacherNull2);

    
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