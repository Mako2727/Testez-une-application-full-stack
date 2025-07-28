package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000); // 1h
    }

    @Test
    void generateJwtToken_shouldCreateToken() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void getUserNameFromJwtToken_shouldReturnUsername() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        String username = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("testuser", username);
    }

 

    @Test
    void validateJwtToken_shouldReturnFalseForInvalidToken() {
        // Un token clairement invalide
        String badToken = "invalid.token.value";

        boolean valid = jwtUtils.validateJwtToken(badToken);

        assertFalse(valid);
    }





@Test
void validateJwtToken_shouldReturnFalseForMalformedToken() {
    String malformedToken = "this.is.not.a.valid.token";
    assertFalse(jwtUtils.validateJwtToken(malformedToken));
}



@Test
void validateJwtToken_shouldReturnFalseForUnsupportedToken() {
    // Simuler un token avec un algorithme non supporté ou format incorrect
    String unsupportedToken = ""; // Ou autre token invalide
    assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
}




  @Test
    void validateJwtToken_shouldReturnTrueForValidToken() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("user");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(auth);

        assertTrue(jwtUtils.validateJwtToken(token));
    }


    @Test
    void validateJwtToken_shouldReturnFalseForMalformedJwt() {
        String malformed = "not.a.jwt.token";

        assertFalse(jwtUtils.validateJwtToken(malformed));
    }

    @Test
    void validateJwtToken_shouldReturnFalseForExpiredJwt() {
        String expiredToken = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))  // expiré il y a 5 sec
                .signWith(SignatureAlgorithm.HS512, "testSecretKey")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

@Test
void validateJwtToken_shouldReturnFalseForUnsupportedJwt() {
    // Création d'un header JWT encodé avec un algorithme non supporté "alg":"fakeAlg"
    String header = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString("{\"alg\":\"fakeAlg\",\"typ\":\"JWT\"}".getBytes());

    // Payload basique (sujet "user")
    String payload = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString("{\"sub\":\"user\"}".getBytes());

    // Signature arbitraire (vide ou incorrecte)
    String signature = "invalidsignature";

    String unsupportedToken = header + "." + payload + "." + signature;

    assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
}

    @Test
    void validateJwtToken_shouldReturnFalseForEmptyToken() {
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken(null));
    }


@Test
void validateJwtToken_shouldReturnFalseForInvalidSignature() {
    String token = Jwts.builder()
        .setSubject("user")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 10000))
        .signWith(SignatureAlgorithm.HS512, "correctSecret")
        .compact();

    // Injection d'une clé incorrecte dans jwtUtils
    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "wrongSecret");

    assertFalse(jwtUtils.validateJwtToken(token));
}

@Test
void validateJwtToken_shouldReturnFalseForExpiredToken() {
    String expiredToken = Jwts.builder()
        .setSubject("expiredUser")
        .setIssuedAt(new Date(System.currentTimeMillis() - 3600000))
        .setExpiration(new Date(System.currentTimeMillis() - 1000))
        .signWith(SignatureAlgorithm.HS512, "testSecretKey")
        .compact();

    assertFalse(jwtUtils.validateJwtToken(expiredToken));
}

@Test
void validateJwtToken_validToken_shouldReturnTrue() {
    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    when(userDetails.getUsername()).thenReturn("testuser");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);

    String token = jwtUtils.generateJwtToken(authentication);

    assertTrue(jwtUtils.validateJwtToken(token));
}

@Test
void validateJwtToken_invalidSignature_shouldReturnFalse() {
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 10000))
        .signWith(SignatureAlgorithm.HS512, "anotherSecret") // good token, wrong key
        .compact();

    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKey");

    assertFalse(jwtUtils.validateJwtToken(token));
}
@Test
void validateJwtToken_malformedToken_shouldReturnFalse() {
    String badToken = "this.is.not.a.jwt";

    assertFalse(jwtUtils.validateJwtToken(badToken));
}

@Test
void validateJwtToken_expiredToken_shouldReturnFalse() {
    String token = Jwts.builder()
        .setSubject("expiredUser")
        .setIssuedAt(new Date(System.currentTimeMillis() - 100000))
        .setExpiration(new Date(System.currentTimeMillis() - 1000)) // expired
        .signWith(SignatureAlgorithm.HS512, "testSecretKey")
        .compact();

    assertFalse(jwtUtils.validateJwtToken(token));
}
}


