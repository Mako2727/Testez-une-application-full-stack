package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.text.SimpleDateFormat;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
    }

    @Test
    void testFindById_found() throws Exception {
        Long id = 1L;
        Session session = new Session();
        SessionDto dto = new SessionDto();

        when(sessionService.getById(id)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));

        verify(sessionService).getById(id);
        verify(sessionMapper).toDto(session);
    }

    @Test
    void testFindById_notFound() throws Exception {
        Long id = 1L;
        when(sessionService.getById(id)).thenReturn(null);

        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(id);
        verifyNoMoreInteractions(sessionMapper);
    }

    @Test
    void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/session/{id}", "notANumber"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService, sessionMapper);
    }

@Test
void testFindAll() throws Exception {
    List<Session> sessions = Arrays.asList(new Session());
    List<SessionDto> dtos = Arrays.asList(new SessionDto());

    when(sessionService.findAll()).thenReturn(sessions);
    when(sessionMapper.toDto(sessions)).thenReturn(dtos);

    mockMvc.perform(get("/api/session"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(dtos)));

    verify(sessionService).findAll();
    verify(sessionMapper).toDto(sessions);
}

    @Test
void testCreate() throws Exception {
    SessionDto inputDto = new SessionDto();
    inputDto.setId(4L);
    inputDto.setName("session de test");

    // Conversion de la date depuis OffsetDateTime en Date
    OffsetDateTime odt = OffsetDateTime.parse("2026-07-18T00:00:00.000+00:00");
    Date date = Date.from(odt.toInstant());
    inputDto.setDate(date);

    inputDto.setTeacher_id(2L);
    inputDto.setDescription("Voici le texte de description.");
    inputDto.setUsers(Collections.emptyList());

    Session entity = new Session();
    SessionDto outputDto = new SessionDto();

    when(sessionMapper.toEntity(inputDto)).thenReturn(entity);
    when(sessionService.create(entity)).thenReturn(entity);
    when(sessionMapper.toDto(entity)).thenReturn(outputDto);

    mockMvc.perform(post("/api/session")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(outputDto)));

    verify(sessionMapper).toEntity(inputDto);
    verify(sessionService).create(entity);
    verify(sessionMapper).toDto(entity);
}

   @Test
void testUpdate_success() throws Exception {
    Long id = 1L;
    SessionDto inputDto = new SessionDto();
    inputDto.setName("session modifiée");

    OffsetDateTime odt = OffsetDateTime.parse("2026-07-19T00:00:00.000+00:00");
    inputDto.setDate(Date.from(odt.toInstant()));

    inputDto.setTeacher_id(3L);
    inputDto.setDescription("Description modifiée");
    inputDto.setUsers(Collections.emptyList());

    Session entity = new Session();
    SessionDto outputDto = new SessionDto();

    when(sessionMapper.toEntity(inputDto)).thenReturn(entity);
    when(sessionService.update(id, entity)).thenReturn(entity);
    when(sessionMapper.toDto(entity)).thenReturn(outputDto);

    mockMvc.perform(put("/api/session/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDto)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(outputDto)));

    verify(sessionMapper).toEntity(inputDto);
    verify(sessionService).update(id, entity);
    verify(sessionMapper).toDto(entity);
}

    @Test
    void testUpdate_badRequest() throws Exception {
        mockMvc.perform(put("/api/session/{id}", "notANumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService, sessionMapper);
    }

    @Test
    void testDelete_success() throws Exception {
        Long id = 1L;
        Session session = new Session();

        when(sessionService.getById(id)).thenReturn(session);
        doNothing().when(sessionService).delete(id);

        mockMvc.perform(delete("/api/session/{id}", id))
                .andExpect(status().isOk());

        verify(sessionService).getById(id);
        verify(sessionService).delete(id);
    }

    @Test
    void testDelete_notFound() throws Exception {
        Long id = 1L;
        when(sessionService.getById(id)).thenReturn(null);

        mockMvc.perform(delete("/api/session/{id}", id))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(id);
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void testDelete_badRequest() throws Exception {
        mockMvc.perform(delete("/api/session/{id}", "notANumber"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService);
    }

    @Test
    void testParticipate_success() throws Exception {
        Long sessionId = 1L;
        Long userId = 2L;

        doNothing().when(sessionService).participate(sessionId, userId);

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId))
                .andExpect(status().isOk());

        verify(sessionService).participate(sessionId, userId);
    }

    @Test
    void testParticipate_badRequest() throws Exception {
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", "bad", "user"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService);
    }

    @Test
    void testNoLongerParticipate_success() throws Exception {
        Long sessionId = 1L;
        Long userId = 2L;

        doNothing().when(sessionService).noLongerParticipate(sessionId, userId);

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId))
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(sessionId, userId);
    }

    @Test
    void testNoLongerParticipate_badRequest() throws Exception {
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", "bad", "user"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService);
    }
}