package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

       @Autowired
    private PasswordEncoder passwordEncoder;

    

    private Session existingSession;
    private String jwtToken;
    private Teacher teacher;
    Long userId ;
    Long sessionId;
    Long userIdParticipate;

    @BeforeEach
    void setUp() throws Exception {
      /* */  sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        //Créer un user pour l'auth
        User user = new User();
        user.setEmail("yoga1@studio.com");
        user.setPassword(passwordEncoder.encode("test!1234")); // mot de passe: test
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAdmin(true); // 
        User savedUser = userRepository.save(user);
         userId = savedUser.getId();

   
        String loginJson = "{ \"email\": \"yoga1@studio.com\", \"password\": \"test!1234\" }";
         System.out.println(" loginJson : " + loginJson);
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

                  System.out.println(" Réponse reçue : " + response);

        JsonNode jsonNode = objectMapper.readTree(response);
        jwtToken = jsonNode.get("token").asText();

        
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);

       
        Session session = new Session();
        session.setName("Yoga Test");
        session.setDescription("Session de test");
        session.setDate(Date.valueOf(LocalDate.now()));
        session.setTeacher(teacher);        
        List<User> users = new ArrayList<>();
        users.add(user);
        session.setUsers(users);
        existingSession = sessionRepository.save(session);  // sauvegarde, l'ID est généré ici
        sessionId = existingSession.getId();    
         userIdParticipate = session.getUsers().get(0).getId();
    }

    @Test
    void testFindById_success() throws Exception {
        mockMvc.perform(get("/api/session/{id}", existingSession.getId())
          .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Test"));
    }

    @Test
    void testFindById_notFound() throws Exception {
        mockMvc.perform(get("/api/session/{id}", 9999)
        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById_badRequest() throws Exception {
        mockMvc.perform(get("/api/session/{id}", "abc")
        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }

   @Test
void testFindAll_success() throws Exception {
   

    // Appelle l'endpoint sécurisé avec le token
    mockMvc.perform(get("/api/session")
            .header("Authorization", "Bearer " + jwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Yoga Test"));
}

    @Test
    void testCreateSession_success() throws Exception {
        SessionDto dto = new SessionDto();
        dto.setName("Nouvelle session");
        dto.setDescription("Description");
        dto.setDate(Date.valueOf(LocalDate.now()));
        dto.setTeacher_id(teacher.getId()); // Prof valide

        mockMvc.perform(post("/api/session")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nouvelle session"));
    }

    @Test
    void testUpdateSession_success() throws Exception {
        SessionDto dto = new SessionDto();
        dto.setName("Session mise à jour");
        dto.setDescription("Update desc");
        dto.setDate(Date.valueOf(LocalDate.now()));
        dto.setTeacher_id(teacher.getId());

        mockMvc.perform(put("/api/session/{id}", existingSession.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Session mise à jour"));
    }

    @Test
    void testDeleteSession_success() throws Exception {
        mockMvc.perform(delete("/api/session/{id}", existingSession.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        assertThat(sessionRepository.findById(existingSession.getId())).isEmpty();
    }

@Test
void testParticipate_success() throws Exception {
    System.out.println("POST /api/session/" + sessionId + "/participate/" + userIdParticipate);
    System.out.println("Authorization: Bearer " + jwtToken);

    String response = mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userIdParticipate)
                    .header("Authorization", "Bearer " + jwtToken))
            .andReturn()
            .getResponse()
            .getContentAsString();

    System.out.println(" Response body: " + response);
}

@Test
void testNoLongerParticipate_success() throws Exception { 

    mockMvc.perform(delete("/api/session/{id}/participate/{userId}",
                        sessionId, userIdParticipate)
                    .header("Authorization", "Bearer " + jwtToken))
            .andExpect(status().isOk());
}
}