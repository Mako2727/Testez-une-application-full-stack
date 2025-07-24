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
    private ObjectMapper objectMapper;

    // Variables réutilisées
    private final Long validId = 1L;
    private Teacher teacher1;
    private Teacher teacher2;
    private TeacherDto dto1;
    private TeacherDto dto2;
    private List<Teacher> teachers;
    private List<TeacherDto> dtos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        // Initialisation des objets communs
        teacher1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacher2 = Teacher.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teachers = Arrays.asList(teacher1, teacher2);

        dto1 = new TeacherDto();
        dto1.setId(teacher1.getId());
        dto1.setFirstName(teacher1.getFirstName());
        dto1.setLastName(teacher1.getLastName());
        dto1.setCreatedAt(teacher1.getCreatedAt());
        dto1.setUpdatedAt(teacher1.getUpdatedAt());

        dto2 = new TeacherDto();
        dto2.setId(teacher2.getId());
        dto2.setFirstName(teacher2.getFirstName());
        dto2.setLastName(teacher2.getLastName());
        dto2.setCreatedAt(teacher2.getCreatedAt());
        dto2.setUpdatedAt(teacher2.getUpdatedAt());

        dtos = Arrays.asList(dto1, dto2);
    }

    @Test
    void testFindById_found() throws Exception {
        when(teacherService.findById(validId)).thenReturn(teacher1);
        when(teacherMapper.toDto(teacher1)).thenReturn(dto1);

        mockMvc.perform(get("/api/teacher/{id}", validId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto1)));

        verify(teacherService).findById(validId);
        verify(teacherMapper).toDto(teacher1);
    }

    @Test
    void testFindById_notFound() throws Exception {
        when(teacherService.findById(validId)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/{id}", validId))
                .andExpect(status().isNotFound());

        verify(teacherService).findById(validId);
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