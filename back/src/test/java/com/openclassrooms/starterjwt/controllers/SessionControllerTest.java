package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    
    private final Long validSessionId = 1L;
    private final Long validUserId = 2L;
    private Session sessionEntity;
    private SessionDto sessionDto;
    private SessionDto inputDto;
    private SessionDto outputDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
        objectMapper = new ObjectMapper();

        
        sessionEntity = new Session();

        sessionDto = new SessionDto();

        inputDto = new SessionDto();
        inputDto.setId(4L);
        inputDto.setName("session de test");
        inputDto.setDate(Date.from(OffsetDateTime.parse("2026-07-18T00:00:00.000+00:00").toInstant()));
        inputDto.setTeacher_id(2L);
        inputDto.setDescription("Voici le texte de description.");
        inputDto.setUsers(Collections.emptyList());

        outputDto = new SessionDto();
    }

    @Test
    void testFindById_found() throws Exception {
        when(sessionService.getById(validSessionId)).thenReturn(sessionEntity);
        when(sessionMapper.toDto(sessionEntity)).thenReturn(sessionDto);

        mockMvc.perform(get("/api/session/{id}", validSessionId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sessionDto)));

        verify(sessionService).getById(validSessionId);
        verify(sessionMapper).toDto(sessionEntity);
    }

    @Test
    void testFindById_notFound() throws Exception {
        when(sessionService.getById(validSessionId)).thenReturn(null);

        mockMvc.perform(get("/api/session/{id}", validSessionId))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(validSessionId);
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
        List<Session> sessions = Collections.singletonList(sessionEntity);
        List<SessionDto> dtos = Collections.singletonList(sessionDto);

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
        when(sessionMapper.toEntity(inputDto)).thenReturn(sessionEntity);
        when(sessionService.create(sessionEntity)).thenReturn(sessionEntity);
        when(sessionMapper.toDto(sessionEntity)).thenReturn(outputDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(outputDto)));

        verify(sessionMapper).toEntity(inputDto);
        verify(sessionService).create(sessionEntity);
        verify(sessionMapper).toDto(sessionEntity);
    }

    @Test
    void testUpdate_success() throws Exception {
        SessionDto updateDto = new SessionDto();
        updateDto.setName("session modifiée");
        updateDto.setDate(Date.from(OffsetDateTime.parse("2026-07-19T00:00:00.000+00:00").toInstant()));
        updateDto.setTeacher_id(3L);
        updateDto.setDescription("Description modifiée");
        updateDto.setUsers(Collections.emptyList());

        Session updatedEntity = new Session();
        SessionDto updatedDto = new SessionDto();

        when(sessionMapper.toEntity(updateDto)).thenReturn(updatedEntity);
        when(sessionService.update(validSessionId, updatedEntity)).thenReturn(updatedEntity);
        when(sessionMapper.toDto(updatedEntity)).thenReturn(updatedDto);

        mockMvc.perform(put("/api/session/{id}", validSessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedDto)));

        verify(sessionMapper).toEntity(updateDto);
        verify(sessionService).update(validSessionId, updatedEntity);
        verify(sessionMapper).toDto(updatedEntity);
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
        when(sessionService.getById(validSessionId)).thenReturn(sessionEntity);
        doNothing().when(sessionService).delete(validSessionId);

        mockMvc.perform(delete("/api/session/{id}", validSessionId))
                .andExpect(status().isOk());

        verify(sessionService).getById(validSessionId);
        verify(sessionService).delete(validSessionId);
    }

    @Test
    void testDelete_notFound() throws Exception {
        when(sessionService.getById(validSessionId)).thenReturn(null);

        mockMvc.perform(delete("/api/session/{id}", validSessionId))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(validSessionId);
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
        doNothing().when(sessionService).participate(validSessionId, validUserId);

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", validSessionId, validUserId))
                .andExpect(status().isOk());

        verify(sessionService).participate(validSessionId, validUserId);
    }

    @Test
    void testParticipate_badRequest() throws Exception {
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", "bad", "user"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService);
    }

    @Test
    void testNoLongerParticipate_success() throws Exception {
        doNothing().when(sessionService).noLongerParticipate(validSessionId, validUserId);

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", validSessionId, validUserId))
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(validSessionId, validUserId);
    }

    @Test
    void testNoLongerParticipate_badRequest() throws Exception {
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", "bad", "user"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(sessionService);
    }

     @Test
    void update_validId_shouldReturnUpdatedSessionDto() {
        String id = "123";

        SessionDto inputDto = new SessionDto();
        

        Session sessionEntity = new Session();
        

        Session updatedSession = new Session();
        

        SessionDto updatedDto = new SessionDto();
        

        when(sessionMapper.toEntity(inputDto)).thenReturn(sessionEntity);
        when(sessionService.update(123L, sessionEntity)).thenReturn(updatedSession);
        when(sessionMapper.toDto(updatedSession)).thenReturn(updatedDto);

        ResponseEntity<?> response = sessionController.update(id, inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedDto, response.getBody());

        verify(sessionMapper).toEntity(inputDto);
        verify(sessionService).update(123L, sessionEntity);
        verify(sessionMapper).toDto(updatedSession);
    }

    @Test
    void update_invalidId_shouldReturnBadRequest() {
        String invalidId = "abc";

        SessionDto inputDto = new SessionDto();

        ResponseEntity<?> response = sessionController.update(invalidId, inputDto);

        assertEquals(400, response.getStatusCodeValue());

        verifyNoInteractions(sessionService);
        verifyNoInteractions(sessionMapper);
    }

}

