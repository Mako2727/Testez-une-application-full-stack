package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        // On récupère l'implémentation générée par MapStruct
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
}