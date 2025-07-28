package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
    }

    @Test
    void testToDto() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.of(2025, 7, 19, 12, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 19, 13, 0))
                .build();

        TeacherDto dto = teacherMapper.toDto(teacher);

        assertNotNull(dto);
        assertEquals(teacher.getId(), dto.getId());
        assertEquals(teacher.getFirstName(), dto.getFirstName());
        assertEquals(teacher.getLastName(), dto.getLastName());
        assertEquals(teacher.getCreatedAt(), dto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void testToEntity() {
        TeacherDto dto = new TeacherDto();
        dto.setId(2L);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setCreatedAt(LocalDateTime.of(2025, 6, 10, 10, 30));
        dto.setUpdatedAt(LocalDateTime.of(2025, 6, 10, 11, 30));

        Teacher teacher = teacherMapper.toEntity(dto);

        assertNotNull(teacher);
        assertEquals(dto.getId(), teacher.getId());
        assertEquals(dto.getFirstName(), teacher.getFirstName());
        assertEquals(dto.getLastName(), teacher.getLastName());
        assertEquals(dto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), teacher.getUpdatedAt());
    }

    @Test
    void testToEntityList() {
        LocalDateTime now = LocalDateTime.now();

        TeacherDto dto1 = new TeacherDto();
        dto1.setId(3L);
        dto1.setFirstName("Alice");
        dto1.setLastName("Smith");
        dto1.setCreatedAt(now);
        dto1.setUpdatedAt(now);

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(4L);
        dto2.setFirstName("Bob");
        dto2.setLastName("Brown");
        dto2.setCreatedAt(now);
        dto2.setUpdatedAt(now);

        List<TeacherDto> dtoList = Arrays.asList(dto1, dto2);

        List<Teacher> teachers = teacherMapper.toEntity(dtoList);

        assertEquals(2, teachers.size());

        Teacher teacher1 = teachers.get(0);
        assertEquals("Alice", teacher1.getFirstName());
        assertEquals("Smith", teacher1.getLastName());

        Teacher teacher2 = teachers.get(1);
        assertEquals("Bob", teacher2.getFirstName());
        assertEquals("Brown", teacher2.getLastName());
    }

    @Test
    void testToDtoList() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher1 = Teacher.builder()
                .id(5L)
                .firstName("Charlie")
                .lastName("Johnson")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(6L)
                .firstName("Diana")
                .lastName("Evans")
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<Teacher> teacherList = Arrays.asList(teacher1, teacher2);

        List<TeacherDto> dtoList = teacherMapper.toDto(teacherList);

        assertEquals(2, dtoList.size());

        TeacherDto dto1 = dtoList.get(0);
        assertEquals("Charlie", dto1.getFirstName());
        assertEquals("Johnson", dto1.getLastName());

        TeacherDto dto2 = dtoList.get(1);
        assertEquals("Diana", dto2.getFirstName());
        assertEquals("Evans", dto2.getLastName());
    }
}