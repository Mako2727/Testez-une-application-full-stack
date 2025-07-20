package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
 void testToDto() {
    User user = User.builder()
            .id(1L)
            .email("user@example.com")
            .firstName("John")
            .lastName("Doe")
            .password("secret123")
            .admin(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    UserDto dto = userMapper.toDto(user);

    assertEquals(user.getId(), dto.getId());
    assertEquals(user.getEmail(), dto.getEmail());
    assertEquals(user.getFirstName(), dto.getFirstName());
    assertEquals(user.getLastName(), dto.getLastName());
    assertEquals(user.isAdmin(), dto.isAdmin());
    // Ici on attend que le password soit copié
    assertEquals("secret123", dto.getPassword());
    assertEquals(user.getCreatedAt(), dto.getCreatedAt());
    assertEquals(user.getUpdatedAt(), dto.getUpdatedAt());
}

    @Test
    void testToEntity() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("user@example.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAdmin(true);
        dto.setPassword("secret123");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        User user = userMapper.toEntity(dto);

        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.isAdmin(), user.isAdmin());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getCreatedAt(), user.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), user.getUpdatedAt());
    }
}