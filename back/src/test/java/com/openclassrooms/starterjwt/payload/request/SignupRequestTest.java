package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

class SignupRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        SignupRequest req = new SignupRequest();
        req.setEmail("test@example.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPassword("secret123");

        assertEquals("test@example.com", req.getEmail());
        assertEquals("John", req.getFirstName());
        assertEquals("Doe", req.getLastName());
        assertEquals("secret123", req.getPassword());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        SignupRequest req1 = new SignupRequest();
        req1.setEmail("a@b.com");
        req1.setFirstName("John");
        req1.setLastName("Doe");
        req1.setPassword("password");

        SignupRequest req2 = new SignupRequest();
        req2.setEmail("a@b.com");
        req2.setFirstName("John");
        req2.setLastName("Doe");
        req2.setPassword("password");

        // equals and hashCode
        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());

        // toString contains email
        assertTrue(req1.toString().contains("a@b.com"));
    }

    @Test
    void testValidationAnnotationsPresence() throws NoSuchFieldException {
        assertTrue(SignupRequest.class.getDeclaredField("email").isAnnotationPresent(NotBlank.class));
        assertTrue(SignupRequest.class.getDeclaredField("email").isAnnotationPresent(Email.class));
        assertTrue(SignupRequest.class.getDeclaredField("email").isAnnotationPresent(Size.class));

        assertTrue(SignupRequest.class.getDeclaredField("firstName").isAnnotationPresent(NotBlank.class));
        assertTrue(SignupRequest.class.getDeclaredField("firstName").isAnnotationPresent(Size.class));

        assertTrue(SignupRequest.class.getDeclaredField("lastName").isAnnotationPresent(NotBlank.class));
        assertTrue(SignupRequest.class.getDeclaredField("lastName").isAnnotationPresent(Size.class));

        assertTrue(SignupRequest.class.getDeclaredField("password").isAnnotationPresent(NotBlank.class));
        assertTrue(SignupRequest.class.getDeclaredField("password").isAnnotationPresent(Size.class));
    }

    @Test
    void testValidSignupRequest() {
        SignupRequest req = new SignupRequest();
        req.setEmail("test@example.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPassword("secret123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty(), "Aucune violation attendue pour un objet valide");
    }

    @Test
    void testInvalidEmail() {
        SignupRequest req = new SignupRequest();
        req.setEmail("not-an-email");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPassword("secret123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Violation attendue pour email invalide");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testBlankFirstName() {
        SignupRequest req = new SignupRequest();
        req.setEmail("test@example.com");
        req.setFirstName("");
        req.setLastName("Doe");
        req.setPassword("secret123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Violation attendue pour firstName vide");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void testPasswordTooShort() {
        SignupRequest req = new SignupRequest();
        req.setEmail("test@example.com");
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setPassword("123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Violation attendue pour mot de passe trop court");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

@Test
void testHashCodeDifferentObjects() {
    SignupRequest req1 = new SignupRequest();
    req1.setEmail("a@b.com");
    req1.setFirstName("John");
    req1.setLastName("Doe");
    req1.setPassword("password");

    SignupRequest req2 = new SignupRequest();
    req2.setEmail("different@b.com");
    req2.setFirstName("Jane");
    req2.setLastName("Smith");
    req2.setPassword("pass123");

    // Même si les objets ne sont pas égaux, hashCode doit fonctionner sans planter
    assertNotEquals(req1.hashCode(), req2.hashCode());
}

@Test
void testHashCodeNullFields() {
    SignupRequest req1 = new SignupRequest();
    SignupRequest req2 = new SignupRequest();

    // Deux objets vides, hashCode ne doit pas planter et être égal
    assertEquals(req1.hashCode(), req2.hashCode());
}

@Test
void testEqualsWithNullAndDifferentClass() {
    SignupRequest req = new SignupRequest();
    req.setEmail("a@b.com");
    req.setFirstName("John");
    req.setLastName("Doe");
    req.setPassword("password");

    // equals doit retourner false si null ou objet d’un autre type
    assertFalse(req.equals(null));
    assertFalse(req.equals("une chaine"));
}
@Test
void testEqualsVariousCases() {
    SignupRequest req1 = new SignupRequest();
    req1.setEmail("a@b.com");
    req1.setFirstName("John");
    req1.setLastName("Doe");
    req1.setPassword("password");

    SignupRequest req2 = new SignupRequest();
    req2.setEmail("a@b.com");
    req2.setFirstName("John");
    req2.setLastName("Doe");
    req2.setPassword("password");

    SignupRequest reqDiff = new SignupRequest();
    reqDiff.setEmail("diff@example.com");
    reqDiff.setFirstName("Jane");
    reqDiff.setLastName("Smith");
    reqDiff.setPassword("otherpassword");

    // Equals to self
    assertTrue(req1.equals(req1));

    // Equals to identical object
    assertTrue(req1.equals(req2));
    assertTrue(req2.equals(req1));

    // Not equals to different object with different fields
    assertFalse(req1.equals(reqDiff));
    assertFalse(reqDiff.equals(req1));

    // Not equals to null
    assertFalse(req1.equals(null));

    // Not equals to object of different type
    assertFalse(req1.equals("some string"));

    // Test equals with null fields in one object
    SignupRequest reqNullFields = new SignupRequest();
    // All fields null by default here

    assertFalse(req1.equals(reqNullFields));
    assertFalse(reqNullFields.equals(req1));

    // Two objects both with null fields should be equal
    SignupRequest reqNullFields2 = new SignupRequest();
    assertTrue(reqNullFields.equals(reqNullFields2));
}

}