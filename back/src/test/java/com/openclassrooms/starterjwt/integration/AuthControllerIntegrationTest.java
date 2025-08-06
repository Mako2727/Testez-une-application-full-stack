package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    
    private User existingUser;
    private User userToAuthenticate;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        
        existingUser = new User();
        existingUser.setEmail("existing@example.com");
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setPassword(passwordEncoder.encode("password"));
        existingUser.setAdmin(false);
        userRepository.save(existingUser);

       
        userToAuthenticate = new User();
        userToAuthenticate.setEmail("yoga@studio.com");
        userToAuthenticate.setFirstName("Admin");
        userToAuthenticate.setLastName("Admin");
        userToAuthenticate.setPassword(passwordEncoder.encode("test!1234"));
        userToAuthenticate.setAdmin(true);
        userRepository.save(userToAuthenticate);
    }

    @Test
    @WithAnonymousUser
    void testRegisterUser_Success() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        int uid = (int) (Math.random() * 900) + 100;
        String uniqueEmail = "marius_" + uid + "@123.fr";
        signupRequest.setEmail(uniqueEmail);
        signupRequest.setPassword("test123");
        signupRequest.setFirstName("Marius");
        signupRequest.setLastName("Test");

        String json = objectMapper.writeValueAsString(signupRequest);
        System.out.println("JSON envoyé : " + json);

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();

        System.out.println("HTTP Status: " + status);
        System.out.println("Response body: " + content);

        assertEquals(200, status, "Le status HTTP devrait être 200 OK");
        assertTrue(content.contains("User registered successfully!"), "Le message de succès attendu");
    }

    @Test
    void testRegisterUser_EmailAlreadyTaken() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(existingUser.getEmail()); 
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userToAuthenticate.getEmail());
        loginRequest.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userToAuthenticate.getEmail()))
                .andExpect(jsonPath("$.admin").value(true))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}