package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private SessionDto sessionDto;
    private Session session;
    private Teacher teacher;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(10L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        user1 = new User();
        user1.setId(100L);

        user2 = new User();
        user2.setId(101L);

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga");
        sessionDto.setDescription("Relaxing session");
        sessionDto.setTeacher_id(10L);
        sessionDto.setDate(new Date());
        sessionDto.setUsers(Arrays.asList(100L, 101L));

        session = new Session();
        session.setId(1L);
        session.setName("Yoga");
        session.setDescription("Relaxing session");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
    }

    @Test
    void testToEntity() {
        when(teacherService.findById(10L)).thenReturn(teacher);
        when(userService.findById(100L)).thenReturn(user1);
        when(userService.findById(101L)).thenReturn(user2);

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);
        assertEquals(sessionDto.getDescription(), result.getDescription());
        assertNotNull(result.getTeacher());
        assertEquals(teacher.getId(), result.getTeacher().getId());
        assertEquals(2, result.getUsers().size());
        assertNotNull(result.getDate());
    }

    @Test
    void testToEntity_withNullTeacherId() {
        sessionDto.setTeacher_id(null);
        when(userService.findById(100L)).thenReturn(user1);
        when(userService.findById(101L)).thenReturn(user2);

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);
        assertNull(result.getTeacher());
        assertEquals(2, result.getUsers().size());
        verify(teacherService, never()).findById(anyLong());
    }

    @Test
    void testToEntity_withNullUsers() {
        sessionDto.setUsers(null);
        when(teacherService.findById(10L)).thenReturn(teacher);

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);
        assertEquals(teacher, result.getTeacher());
        assertNotNull(result.getUsers());
        assertTrue(result.getUsers().isEmpty());
    }

    @Test
    void testToEntity_userServiceReturnsNull() {
        when(teacherService.findById(10L)).thenReturn(teacher);
        when(userService.findById(100L)).thenReturn(user1);
        when(userService.findById(101L)).thenReturn(null);

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);
        assertEquals(teacher, result.getTeacher());
        assertEquals(2, result.getUsers().size());
        assertEquals(user1, result.getUsers().get(0));
        assertNull(result.getUsers().get(1));
    }

    @Test
    void testToDto() {
        SessionDto dto = sessionMapper.toDto(session);

        assertNotNull(dto);
        assertEquals(session.getDescription(), dto.getDescription());
        assertEquals(teacher.getId(), dto.getTeacher_id());
        assertEquals(2, dto.getUsers().size());
        assertTrue(dto.getUsers().contains(user1.getId()));
        assertTrue(dto.getUsers().contains(user2.getId()));
        assertNotNull(dto.getDate());
    }

    @Test
    void testToDto_withNullTeacher() {
        session.setTeacher(null);

        SessionDto dto = sessionMapper.toDto(session);

        assertNotNull(dto);
        assertNull(dto.getTeacher_id());
    }

    @Test
    void testToDto_withNullUsers() {
        session.setUsers(null);

        SessionDto dto = sessionMapper.toDto(session);

        assertNotNull(dto);
        assertNotNull(dto.getUsers());
        assertTrue(dto.getUsers().isEmpty());
    }

    @Test
void testToEntityList() {
    
    SessionDto dto1 = new SessionDto();
    dto1.setId(1L);
    dto1.setName("Yoga");
    dto1.setDescription("Session 1");
    dto1.setTeacher_id(10L);
    dto1.setDate(new Date());
    dto1.setUsers(Arrays.asList(100L, 101L));

    SessionDto dto2 = new SessionDto();
    dto2.setId(2L);
    dto2.setName("Pilates");
    dto2.setDescription("Session 2");
    dto2.setTeacher_id(null);  
    dto2.setDate(new Date());
    dto2.setUsers(Collections.singletonList(100L));

    
    when(teacherService.findById(10L)).thenReturn(teacher);
    when(userService.findById(100L)).thenReturn(user1);
    when(userService.findById(101L)).thenReturn(user2);

    
    List<Session> sessions = sessionMapper.toEntity(Arrays.asList(dto1, dto2));

    assertNotNull(sessions);
    assertEquals(2, sessions.size());

    
    Session s1 = sessions.get(0);
    assertEquals("Yoga", s1.getName());
    assertNotNull(s1.getTeacher());
    assertEquals(teacher.getId(), s1.getTeacher().getId());
    assertEquals(2, s1.getUsers().size());
    assertTrue(s1.getUsers().contains(user1));
    assertTrue(s1.getUsers().contains(user2));

    
    Session s2 = sessions.get(1);
    assertEquals("Pilates", s2.getName());
    assertNull(s2.getTeacher()); 
    assertEquals(1, s2.getUsers().size());
    assertTrue(s2.getUsers().contains(user1));
}

@Test
void testToDto_withTeacherButNoId() {
    Teacher teacherWithoutId = new Teacher();
    session.setTeacher(teacherWithoutId);

    SessionDto dto = sessionMapper.toDto(session);

    assertNotNull(dto);
    assertNull(dto.getTeacher_id()); 
}
}