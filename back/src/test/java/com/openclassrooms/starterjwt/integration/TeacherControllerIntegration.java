package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TeacherControllerIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher existingTeacher;
    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        // Créer un utilisateur pour l'authentification
        User user = new User();
        user.setEmail("yoga1@studio.com");
        user.setPassword(passwordEncoder.encode("test!1234"));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAdmin(true);
        userRepository.save(user);

        // Authentification pour obtenir le token JWT
        String loginJson = "{ \"email\": \"yoga1@studio.com\", \"password\": \"test!1234\" }";
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        jwtToken = jsonNode.get("token").asText();

        // Créer deux enseignants
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        existingTeacher = teacherRepository.save(teacher);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");
        teacherRepository.save(teacher2);
    }

    @Test
    void testFindById_success() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", existingTeacher.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testFindById_notFound() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", 9999)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", "abc")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindAll_success() throws Exception {
        mockMvc.perform(get("/api/teacher")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }
}