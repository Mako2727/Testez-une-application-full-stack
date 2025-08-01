package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

class UserDetailsImplTest {

    @Test
    void testGettersAndProperties() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("jdoe")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("secret")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("jdoe", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertTrue(user.getAdmin());
        assertEquals("secret", user.getPassword());

        // Authorities should be empty HashSet
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities instanceof HashSet);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testAccountStatusMethods() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

@Test
void testEquals() {
    UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
    UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
    UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

    // Cas objet égal à lui-même
    assertEquals(user1, user1);

    assertEquals(user1, user2);
    assertNotEquals(user1, user3);
    assertNotEquals(user1, null);
    assertNotEquals(user1, "some string");
}

    @Test
    void testUserDetailsImplBuilderToString() {
        UserDetailsImpl.UserDetailsImplBuilder builder = UserDetailsImpl.builder()
            .id(123L)
            .username("user@example.com")
            .firstName("John")
            .lastName("Doe")
            .admin(true)
            .password("secret");

        String toStringResult = builder.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("id=123"));
        assertTrue(toStringResult.contains("username=user@example.com"));
        assertTrue(toStringResult.contains("firstName=John"));
        assertTrue(toStringResult.contains("lastName=Doe"));
        assertTrue(toStringResult.contains("admin=true"));
        assertTrue(toStringResult.contains("password=secret"));
    }
}
