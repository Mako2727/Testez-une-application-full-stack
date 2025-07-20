package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        session = Session.builder()
                .id(1L)
                .name("Session 1")
                .description("Description 1")
                .date(new Date())
                .users(new ArrayList<>())
                .build();
    }

    @Test
    void testCreate() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session created = sessionService.create(session);

        assertNotNull(created);
        assertEquals(session.getId(), created.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testDelete() {
        doNothing().when(sessionRepository).deleteById(session.getId());

        sessionService.delete(session.getId());

        verify(sessionRepository, times(1)).deleteById(session.getId());
    }

    @Test
    void testFindAll() {
        List<Session> sessions = Arrays.asList(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> found = sessionService.findAll();

        assertEquals(1, found.size());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void testGetById_found() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        Session found = sessionService.getById(session.getId());

        assertNotNull(found);
        assertEquals(session.getId(), found.getId());
        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    void testGetById_notFound() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        Session found = sessionService.getById(session.getId());

        assertNull(found);
        verify(sessionRepository, times(1)).findById(session.getId());
    }

    @Test
    void testUpdate() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session updated = sessionService.update(session.getId(), session);

        assertNotNull(updated);
        assertEquals(session.getId(), updated.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipate_success() {
        session.getUsers().clear();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.participate(session.getId(), user.getId());

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void testParticipate_sessionNotFound() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), user.getId());
        });
    }

    @Test
    void testParticipate_userNotFound() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(session.getId(), user.getId());
        });
    }

    @Test
    void testParticipate_alreadyParticipate() {
        session.getUsers().add(user);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(session.getId(), user.getId());
        });
    }

    @Test
    void testNoLongerParticipate_success() {
        session.getUsers().add(user);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.noLongerParticipate(session.getId(), user.getId());

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void testNoLongerParticipate_sessionNotFound() {
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(session.getId(), user.getId());
        });
    }

    @Test
    void testNoLongerParticipate_notParticipating() {
        session.getUsers().clear();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(session.getId(), user.getId());
        });
    }
}