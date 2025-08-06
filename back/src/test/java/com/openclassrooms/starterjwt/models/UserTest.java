package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(1L)
            .setEmail("user@example.com")
            .setFirstName("John")
            .setLastName("Doe")
            .setPassword("secret123")
            .setAdmin(true)
            .setCreatedAt(now)
            .setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("secret123", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());

        
        User user2 = new User();
        user2.setId(1L);

        assertEquals(user, user2);
        assertEquals(user.hashCode(), user2.hashCode());

        
        String toString = user.toString();
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("email=user@example.com"));
        assertTrue(toString.contains("firstName=John"));
        assertTrue(toString.contains("lastName=Doe"));
    }

@Test
void testUserBuilder() {
    LocalDateTime now = LocalDateTime.now();

    User user = User.builder()
            .id(1L)
            .email("user@example.com")
            .firstName("John")
            .lastName("Doe")
            .password("secret123")
            .admin(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

    assertEquals(1L, user.getId());
    assertEquals("user@example.com", user.getEmail());
    assertEquals("John", user.getFirstName());
    assertEquals("Doe", user.getLastName());
    assertEquals("secret123", user.getPassword());
    assertTrue(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}
@Test
void testRequiredArgsConstructor_coversAllParams() {
    User user = new User(
        "constructed@example.com",
        "Lastname",
        "Firstname",
        "pwd123",
        false
    );

    assertNotNull(user);
    assertEquals("constructed@example.com", user.getEmail());
    assertEquals("Lastname", user.getLastName());
    assertEquals("Firstname", user.getFirstName());
    assertEquals("pwd123", user.getPassword());
    assertFalse(user.isAdmin());

    
    assertNull(user.getId());
    assertNull(user.getCreatedAt());
    assertNull(user.getUpdatedAt());
}
@Test
void testSetFirstName_explicitValues() {
    User user = new User();

    
    user.setFirstName("Alice");
    assertEquals("Alice", user.getFirstName());

    
    user.setFirstName("");
    assertEquals("", user.getFirstName());

    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 20; i++) {
        sb.append("A");
    }
    String longFirstName = sb.toString();
    user.setFirstName(longFirstName);
    assertEquals(longFirstName, user.getFirstName());

    
    assertThrows(NullPointerException.class, () -> user.setFirstName(null));
}
@Test
void testSetLastName_explicitValues() {
    User user = new User();

    
    user.setLastName("Dupont");
    assertEquals("Dupont", user.getLastName());

    
    user.setLastName("");
    assertEquals("", user.getLastName());

    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 20; i++) {
        sb.append("Z");
    }
    String longLastName = sb.toString();
    user.setLastName(longLastName);
    assertEquals(longLastName, user.getLastName());

    
    assertThrows(NullPointerException.class, () -> user.setLastName(null));
}
@Test
void testSetPassword_explicitValues() {
    User user = new User();

    
    user.setPassword("mySecurePassword123");
    assertEquals("mySecurePassword123", user.getPassword());

    
    user.setPassword("");
    assertEquals("", user.getPassword());

    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 120; i++) {
        sb.append("X");
    }
    String longPassword = sb.toString();
    user.setPassword(longPassword);
    assertEquals(longPassword, user.getPassword());

    
    assertThrows(NullPointerException.class, () -> user.setPassword(null));
}

@Test
void testEquals() {
    User user1 = new User();
    user1.setId(1L);

    User user2 = new User();
    user2.setId(1L);

    User user3 = new User();
    user3.setId(2L);

    
    assertEquals(user1, user1);

    
    assertEquals(user1, user2);
    assertEquals(user2, user1);

   
    User user4 = new User();
    user4.setId(1L);
    assertEquals(user1, user4);
    assertEquals(user2, user4);

    
    assertNotEquals(user1, user3);

    
    assertNotEquals(user1, null);

    
    assertNotEquals(user1, "une chaîne");

 
}
@Test
void testHashCode() {
    User user1 = new User();
    user1.setId(1L);

    User user2 = new User();
    user2.setId(1L);

    User user3 = new User();
    user3.setId(2L);

    User userNull1 = new User();
    User userNull2 = new User();

    
    assertEquals(user1.hashCode(), user2.hashCode());

    
    assertNotEquals(user1.hashCode(), user3.hashCode());

   
    assertEquals(userNull1.hashCode(), userNull2.hashCode());
}

@Test
void testUserBuilderToString() {
    LocalDateTime now = LocalDateTime.now();

    User.UserBuilder builder = User.builder()
        .id(123L)
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .password("secret")
        .admin(true)
        .createdAt(now)
        .updatedAt(now);

    String toString = builder.toString();

    assertNotNull(toString);
    assertTrue(toString.contains("id=123"));
    assertTrue(toString.contains("email=test@example.com"));
    assertTrue(toString.contains("firstName=John"));
    assertTrue(toString.contains("lastName=Doe"));
    assertTrue(toString.contains("password=secret"));
    assertTrue(toString.contains("admin=true"));
    assertTrue(toString.contains("createdAt="));
    assertTrue(toString.contains("updatedAt="));
}

@Test
void testSetLastName() {
    User user = new User();

    
    String validLastName = "Dupont";
    user.setLastName(validLastName);
    assertEquals(validLastName, user.getLastName());

    
    String emptyLastName = "";
    user.setLastName(emptyLastName);
    assertEquals(emptyLastName, user.getLastName());

    
    assertThrows(NullPointerException.class, () -> user.setLastName(null));
}

@Test
void testSetEmail() {
    User user = new User();

    
    String validEmail = "exemple@test.com";
    user.setEmail(validEmail);
    assertEquals(validEmail, user.getEmail());

    
    String emptyEmail = "";
    user.setEmail(emptyEmail);
    assertEquals(emptyEmail, user.getEmail());

    
    assertThrows(NullPointerException.class, () -> user.setEmail(null));
}

@Test
void testEqualsAndHashCode_sameId() {
    User user1 = new User().setId(1L);
    User user2 = new User().setId(1L);

    assertEquals(user1, user2);
    assertEquals(user1.hashCode(), user2.hashCode());
}

@Test
void testEqualsAndHashCode_differentId() {
    User user1 = new User().setId(1L);
    User user2 = new User().setId(2L);

    assertNotEquals(user1, user2);
    assertNotEquals(user1.hashCode(), user2.hashCode());
}

@Test
void testEqualsAndHashCode_nullId() {
    User user1 = new User().setId(null);
    User user2 = new User().setId(null);

    
    assertEquals(user1, user2);
    assertEquals(user1.hashCode(), user2.hashCode());

    
    assertEquals(user1, user1);
}

@Test
void testEquals_nullAndOtherClass() {
    User user = new User().setId(1L);

    assertNotEquals(user, null);
    assertNotEquals(user, "string");
}

@Test
void testEquals_sameObject() {
    User user = new User().setId(1L);

    assertEquals(user, user);
}



@Test
void testUserBuilderSetEmail() {
    LocalDateTime now = LocalDateTime.now();

    
    User user = User.builder()
            .email("builder.email@example.com")
            .firstName("John")
            .lastName("Doe")
            .password("password123")
            .admin(false)
            .createdAt(now)
            .updatedAt(now)
            .build();

    
    assertEquals("builder.email@example.com", user.getEmail());

    
    assertEquals("John", user.getFirstName());
    assertEquals("Doe", user.getLastName());
    assertEquals("password123", user.getPassword());
    assertFalse(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}

@Test
void testUserBuilderEmailNullThrowsException() {
    LocalDateTime now = LocalDateTime.now();

    
    assertThrows(NullPointerException.class, () -> {
        User.builder()
            .email(null)
            .firstName("John")
            .lastName("Doe")
            .password("password123")
            .admin(false)
            .createdAt(now)
            .updatedAt(now)
            .build();
    });
}

@Test
void testUserBuilderSetFirstName() {
    LocalDateTime now = LocalDateTime.now();

    User user = User.builder()
            .email("test@example.com")  
            .firstName("BuilderFirstName")
            .lastName("Doe")
            .password("password123")
            .admin(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

    assertEquals("BuilderFirstName", user.getFirstName());
    assertEquals("test@example.com", user.getEmail());
    assertEquals("Doe", user.getLastName());
    assertEquals("password123", user.getPassword());
    assertTrue(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}

@Test
void testUserBuilderFirstNameNullThrowsException() {
    LocalDateTime now = LocalDateTime.now();

    
    assertThrows(NullPointerException.class, () -> {
        User.builder()
            .email("test@example.com")
            .firstName(null)
            .lastName("Doe")
            .password("password123")
            .admin(false)
            .createdAt(now)
            .updatedAt(now)
            .build();
    });
}

@Test
void testUserBuilderSetLastName() {
    LocalDateTime now = LocalDateTime.now();

    User user = User.builder()
            .email("test@example.com")  
            .firstName("John")
            .lastName("BuilderLastName")
            .password("password123")
            .admin(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

    assertEquals("BuilderLastName", user.getLastName());
    assertEquals("John", user.getFirstName());
    assertEquals("test@example.com", user.getEmail());
    assertEquals("password123", user.getPassword());
    assertTrue(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}

@Test
void testUserBuilderLastNameNullThrowsException() {
    LocalDateTime now = LocalDateTime.now();

    
    assertThrows(NullPointerException.class, () -> {
        User.builder()
            .email("test@example.com")
            .firstName("John")
            .lastName(null)
            .password("password123")
            .admin(false)
            .createdAt(now)
            .updatedAt(now)
            .build();
    });
}

@Test
void testUserBuilderSetPassword() {
    LocalDateTime now = LocalDateTime.now();

    User user = User.builder()
            .email("test@example.com")  
            .firstName("John")
            .lastName("Doe")
            .password("MySecretPass123")
            .admin(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

    assertEquals("MySecretPass123", user.getPassword());
    assertEquals("John", user.getFirstName());
    assertEquals("Doe", user.getLastName());
    assertEquals("test@example.com", user.getEmail());
    assertTrue(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}

@Test
void testUserBuilderPasswordNullThrowsException() {
    LocalDateTime now = LocalDateTime.now();

  
    assertThrows(NullPointerException.class, () -> {
        User.builder()
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .password(null)
            .admin(false)
            .createdAt(now)
            .updatedAt(now)
            .build();
    });
}

@Test
void testAllArgsConstructor() {
    LocalDateTime now = LocalDateTime.now();

    User user = new User(
        123L,
        "test@example.com",
        "Doe",
        "John",
        "securePass123",
        true,
        now,
        now
    );

    assertEquals(123L, user.getId());
    assertEquals("test@example.com", user.getEmail());
    assertEquals("Doe", user.getLastName());
    assertEquals("John", user.getFirstName());
    assertEquals("securePass123", user.getPassword());
    assertTrue(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}

@Test
void testAllArgsConstructorWithoutNullsAndFalse() {
    LocalDateTime now = LocalDateTime.now();

    User user = new User(
        null,                      
        "test@example.com",       
        "Doe",                   
        "John",                   
        "secret",                
        false,
        now,
        now
    );

    assertNull(user.getId());
    assertEquals("test@example.com", user.getEmail());
    assertEquals("Doe", user.getLastName());
    assertEquals("John", user.getFirstName());
    assertEquals("secret", user.getPassword());
    assertFalse(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}
@Test
void testAllArgsConstructorWithoutNullForNonNullFields() {
    LocalDateTime now = LocalDateTime.now();

    User user = new User(
        1L,
        "email@example.com",  
        "LastName",          
        "FirstName",          
        "password",           
        false,
        now,
        now
    );

    assertEquals(1L, user.getId());
    assertEquals("email@example.com", user.getEmail());
    assertEquals("LastName", user.getLastName());
    assertEquals("FirstName", user.getFirstName());
    assertEquals("password", user.getPassword());
    assertFalse(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}
@Test
void testAllArgsConstructorThrowsExceptionOnNullEmail() {
    LocalDateTime now = LocalDateTime.now();

    assertThrows(NullPointerException.class, () -> {
        new User(
            1L,
            null,  
            "LastName",
            "FirstName",
            "password",
            false,
            now,
            now
        );
    });
}

@Test
void testAllArgsConstructorThrowsOnNullEmail() {
    LocalDateTime now = LocalDateTime.now();
    assertThrows(NullPointerException.class, () -> new User(null, null, "Doe", "John", "secret", false, now, now));
}

@Test
void testAllArgsConstructorThrowsOnNullLastName() {
    LocalDateTime now = LocalDateTime.now();
    assertThrows(NullPointerException.class, () -> new User(null, "test@example.com", null, "John", "secret", false, now, now));
}

@Test
void testAllArgsConstructorThrowsOnNullFirstName() {
    LocalDateTime now = LocalDateTime.now();
    assertThrows(NullPointerException.class, () -> new User(null, "test@example.com", "Doe", null, "secret", false, now, now));
}

@Test
void testAllArgsConstructorThrowsOnNullPassword() {
    LocalDateTime now = LocalDateTime.now();
    assertThrows(NullPointerException.class, () -> new User(null, "test@example.com", "Doe", "John", null, false, now, now));
}

@Test
void testRequiredArgsConstructor() {
    
    User user = new User("test@example.com", "Doe", "John", "secret", true);

    assertEquals("test@example.com", user.getEmail());
    assertEquals("Doe", user.getLastName());
    assertEquals("John", user.getFirstName());
    assertEquals("secret", user.getPassword());
    assertTrue(user.isAdmin());

   
    assertNull(user.getId());
    assertNull(user.getCreatedAt());
    assertNull(user.getUpdatedAt());
}

@Test
void testRequiredArgsConstructorThrowsOnNullEmail() {
    assertThrows(NullPointerException.class, () -> new User(null, "Doe", "John", "secret", true));
}

@Test
void testRequiredArgsConstructorThrowsOnNullLastName() {
    assertThrows(NullPointerException.class, () -> new User("test@example.com", null, "John", "secret", true));
}

@Test
void testRequiredArgsConstructorThrowsOnNullFirstName() {
    assertThrows(NullPointerException.class, () -> new User("test@example.com", "Doe", null, "secret", true));
}

@Test
void testRequiredArgsConstructorThrowsOnNullPassword() {
    assertThrows(NullPointerException.class, () -> new User("test@example.com", "Doe", "John", null, true));
}

@Test
void testEquals_sameId_shouldBeEqual() {
    User user1 = new User();
    user1.setId(1L);

    User user2 = new User();
    user2.setId(1L);

    assertEquals(user1, user2);
    assertEquals(user1.hashCode(), user2.hashCode());
}

@Test
void testEquals_differentIds_shouldNotBeEqual() {
    User user1 = new User();
    user1.setId(1L);

    User user2 = new User();
    user2.setId(2L);

    assertNotEquals(user1, user2);
}

@Test
void testEquals_sameObject_shouldBeEqual() {
    User user = new User();
    user.setId(1L);

    assertEquals(user, user);
}

@Test
void testEquals_null_shouldNotBeEqual() {
    User user = new User();
    user.setId(1L);

    assertNotEquals(user, null);
}

@Test
void testEquals_differentClass_shouldNotBeEqual() {
    User user = new User();
    user.setId(1L);

    String otherObject = "Not a User";

    assertNotEquals(user, otherObject);
}

@Test
void testEquals_nullIdComparison_shouldNotBeEqual() {
    User user1 = new User(); 
    User user2 = new User();
    user2.setId(2L);

    assertNotEquals(user1, user2);
}

@Test
void testEquals_bothIdsNull_shouldBeEqual() {
    User user1 = new User();
    User user2 = new User();

    assertEquals(user1, user2);
}

@Test
void testEquals_withDifferentType_shouldReturnFalse() {
    User user = new User();
    String notAUser = "Not a user";
    assertNotEquals(user, notAUser);
}

@Test
void testEquals_withNull_shouldReturnFalse() {
    User user = new User();
    assertNotEquals(user, null);
}

@Test
void testEquals_sameReference_shouldReturnTrue() {
    User user = new User();
    assertEquals(user, user);
}

@Test
void testHashCode_bothIdsNull_shouldBeEqual() {
    User user1 = new User();
    User user2 = new User();
    assertEquals(user1.hashCode(), user2.hashCode());
}
@Test
void testBuilder_withAllFields() {
    LocalDateTime now = LocalDateTime.now();

    User user = User.builder()
            .id(42L)
            .email("builder@test.com")
            .lastName("Builder")
            .firstName("Test")
            .password("pass123")
            .admin(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

    assertEquals(42L, user.getId());
    assertEquals("builder@test.com", user.getEmail());
    assertEquals("Builder", user.getLastName());
    assertEquals("Test", user.getFirstName());
    assertEquals("pass123", user.getPassword());
    assertTrue(user.isAdmin());
    assertEquals(now, user.getCreatedAt());
    assertEquals(now, user.getUpdatedAt());
}

@Test
void testToString_containsEmail() {
    User user = User.builder()
            .id(1L)
            .email("email@test.com")
            .lastName("Last")
            .firstName("First")
            .password("pass")
            .admin(true)
            .build();

    String str = user.toString();
    assertNotNull(str);
    assertTrue(str.contains("email=test.com") || str.contains("email="));
}

}