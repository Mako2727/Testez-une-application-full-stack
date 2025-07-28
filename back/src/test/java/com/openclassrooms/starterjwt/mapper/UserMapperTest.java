package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

    @Test
    void testToEntityList() {
        LocalDateTime now = LocalDateTime.now();

        UserDto dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setEmail("user1@example.com");
        dto1.setFirstName("Alice");
        dto1.setLastName("Smith");
        dto1.setPassword("pwd1");
        dto1.setAdmin(false);
        dto1.setCreatedAt(now);
        dto1.setUpdatedAt(now);

        UserDto dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setEmail("user2@example.com");
        dto2.setFirstName("Bob");
        dto2.setLastName("Brown");
        dto2.setPassword("pwd2");
        dto2.setAdmin(true);
        dto2.setCreatedAt(now);
        dto2.setUpdatedAt(now);

        List<UserDto> dtoList = Arrays.asList(dto1, dto2);

        List<User> users = userMapper.toEntity(dtoList);

        assertEquals(2, users.size());

        User user1 = users.get(0);
        assertEquals("Alice", user1.getFirstName());
        assertEquals("user1@example.com", user1.getEmail());

        User user2 = users.get(1);
        assertEquals("Bob", user2.getFirstName());
        assertEquals("user2@example.com", user2.getEmail());
    }

    @Test
    void testToDtoList() {
        LocalDateTime now = LocalDateTime.now();

        User user1 = User.builder()
                .id(1L)
                .email("user1@example.com")
                .firstName("Alice")
                .lastName("Smith")
                .password("pwd1")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("user2@example.com")
                .firstName("Bob")
                .lastName("Brown")
                .password("pwd2")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<User> userList = Arrays.asList(user1, user2);

        List<UserDto> dtoList = userMapper.toDto(userList);

        assertEquals(2, dtoList.size());

        UserDto dto1 = dtoList.get(0);
        assertEquals("Alice", dto1.getFirstName());
        assertEquals("user1@example.com", dto1.getEmail());

        UserDto dto2 = dtoList.get(1);
        assertEquals("Bob", dto2.getFirstName());
        assertEquals("user2@example.com", dto2.getEmail());
    }
}