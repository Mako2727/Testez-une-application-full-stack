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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testFindById_found() throws Exception {
        Long id = 1L;

        User user = new User();
        user.setId(id);
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail("user@example.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAdmin(true);
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        when(userService.findById(id)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        mockMvc.perform(get("/api/user/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));

        verify(userService).findById(id);
        verify(userMapper).toDto(user);
    }

    @Test
    void testFindById_notFound() throws Exception {
        Long id = 1L;
        when(userService.findById(id)).thenReturn(null);

        mockMvc.perform(get("/api/user/{id}", id))
                .andExpect(status().isNotFound());

        verify(userService).findById(id);
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
        Long userId = 1L;

        User user = new User();
        user.setEmail("user@example.com");
        when(userService.findById(userId)).thenReturn(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        doNothing().when(userService).delete(userId);

        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isOk());

        verify(userService).findById(userId);
        verify(userService).delete(userId);
    }

    @Test
void testDelete_unauthorized() throws Exception {
    Long id = 1L;

    // Simuler un utilisateur trouvé en base
    User user = new User();
    user.setId(id);
    user.setEmail("user@example.com");

    when(userService.findById(id)).thenReturn(user);

    // Simuler l'utilisateur connecté (différent => non autorisé)
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetails.getUsername()).thenReturn("otheruser@example.com");
    when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

    // Construire l'objet Authentication avec les infos mockées
    Authentication authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    // Mock du SecurityContext
    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);

    // Définir le contexte de sécurité avec le mock
    SecurityContextHolder.setContext(securityContext);

    // Appeler le contrôleur avec une requête DELETE
    mockMvc.perform(delete("/api/user/{id}", id))
            .andExpect(status().isUnauthorized());

    // Vérifier les interactions
    verify(userService).findById(id);
    verify(userService, never()).delete(anyLong());
}
    @Test
    void testDelete_notFound() throws Exception {
        Long id = 1L;
        when(userService.findById(id)).thenReturn(null);

        mockMvc.perform(delete("/api/user/{id}", id))
                .andExpect(status().isNotFound());

        verify(userService).findById(id);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void testDelete_badRequest() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", "notANumber"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }
}