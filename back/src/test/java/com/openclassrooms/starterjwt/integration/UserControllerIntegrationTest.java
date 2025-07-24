package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  // application-test.properties avec H2
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    private User existingUser;
 User otherUser;
    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();

        // Création utilisateur avec mot de passe encodé
        User user = new User();
        user.setEmail("user@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword(passwordEncoder.encode("test1234"));
        user = userRepository.save(user);
        existingUser = user;

        // Login pour récupérer token JWT
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@test.com");
        loginRequest.setPassword("test1234");

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(response).get("token").asText();

        // Création d'un autre utilisateur
        otherUser = new User();
        otherUser.setEmail("other@test.com");
        otherUser.setFirstName("Other");
        otherUser.setLastName("User");
        otherUser.setPassword(passwordEncoder.encode("password"));
        otherUser = userRepository.save(otherUser);
    }

    @Test
    void testFindById_success() throws Exception {
        mockMvc.perform(get("/api/user/{id}", existingUser.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    @Test
    void testFindById_notFound() throws Exception {
        mockMvc.perform(get("/api/user/{id}", 9999L)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "abc")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUser_success() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", existingUser.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(existingUser.getId())).isEmpty();
    }

    @Test
    void testDeleteUser_unauthorized() throws Exception {
        

        // Essayer de supprimer otherUser avec le token de existingUser => doit être 401
        mockMvc.perform(delete("/api/user/{id}", otherUser.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isUnauthorized());

        assertThat(userRepository.findById(otherUser.getId())).isPresent();
    }

    @Test
    void testDeleteUser_badRequest() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", "abc")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }
}
