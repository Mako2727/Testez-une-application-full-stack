package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void testFindById_found() throws Exception {
        Long id = 1L;
        Teacher teacher = Teacher.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.of(2025, 7, 13, 23, 23, 23))
                .updatedAt(LocalDateTime.of(2025, 7, 13, 23, 23, 23))
                .build();

        TeacherDto dto = new TeacherDto();
        dto.setId(id);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setCreatedAt(teacher.getCreatedAt());
        dto.setUpdatedAt(teacher.getUpdatedAt());

        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(dto);

        mockMvc.perform(get("/api/teacher/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));

        verify(teacherService).findById(id);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    void testFindById_notFound() throws Exception {
        Long id = 1L;
        when(teacherService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/{id}", id))
                .andExpect(status().isNotFound());

        verify(teacherService).findById(id);
        verifyNoMoreInteractions(teacherMapper);
    }

    @Test
    void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", "notANumber"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(teacherService, teacherMapper);
    }

    @Test
    void testFindAll() throws Exception {
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setCreatedAt(teacher1.getCreatedAt());
        dto1.setUpdatedAt(teacher1.getUpdatedAt());

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");
        dto2.setCreatedAt(teacher2.getCreatedAt());
        dto2.setUpdatedAt(teacher2.getUpdatedAt());

        List<TeacherDto> dtos = Arrays.asList(dto1, dto2);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(dtos);

        mockMvc.perform(get("/api/teacher")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtos)));

        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teachers);
    }
}