package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final Long validId = 1L;
    private User user;
    private UserDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setId(validId);
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        dto = new UserDto();
        dto.setId(validId);
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAdmin(user.isAdmin());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
    }

    @Test
    void testFindById_found() throws Exception {
        when(userService.findById(validId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        mockMvc.perform(get("/api/user/{id}", validId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));

        verify(userService).findById(validId);
        verify(userMapper).toDto(user);
    }

    @Test
    void testFindById_notFound() throws Exception {
        when(userService.findById(validId)).thenReturn(null);

        mockMvc.perform(get("/api/user/{id}", validId))
                .andExpect(status().isNotFound());

        verify(userService).findById(validId);
        verifyNoMoreInteractions(userMapper);
    }

    @Test
    void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "notANumber"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService, userMapper);
    }

    @Test
    void testDelete_success() throws Exception {
        when(userService.findById(validId)).thenReturn(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(user.getEmail());
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        doNothing().when(userService).delete(validId);

        mockMvc.perform(delete("/api/user/{id}", validId))
                .andExpect(status().isOk());

        verify(userService).findById(validId);
        verify(userService).delete(validId);
    }

    @Test
    void testDelete_unauthorized() throws Exception {
        when(userService.findById(validId)).thenReturn(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("otheruser@example.com"); 
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/api/user/{id}", validId))
                .andExpect(status().isUnauthorized());

        verify(userService).findById(validId);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void testDelete_notFound() throws Exception {
        when(userService.findById(validId)).thenReturn(null);

        mockMvc.perform(delete("/api/user/{id}", validId))
                .andExpect(status().isNotFound());

        verify(userService).findById(validId);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void testDelete_badRequest() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", "notANumber"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }
}