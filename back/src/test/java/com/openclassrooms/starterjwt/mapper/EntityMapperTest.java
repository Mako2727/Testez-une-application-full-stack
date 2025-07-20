package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EntityMapperTest {

    static class DummyDto {
        public String value;

        public DummyDto(String value) {
            this.value = value;
        }
    }

    static class DummyEntity {
        public String value;

        public DummyEntity(String value) {
            this.value = value;
        }
    }

    static class DummyMapper implements EntityMapper<DummyDto, DummyEntity> {

        @Override
        public DummyEntity toEntity(DummyDto dto) {
            return dto == null ? null : new DummyEntity(dto.value);
        }

        @Override
        public DummyDto toDto(DummyEntity entity) {
            return entity == null ? null : new DummyDto(entity.value);
        }

@Override
public List<DummyEntity> toEntity(List<DummyDto> dtoList) {
    return dtoList == null ? null : dtoList.stream().map(this::toEntity).collect(Collectors.toList());
}

@Override
public List<DummyDto> toDto(List<DummyEntity> entityList) {
    return entityList == null ? null : entityList.stream().map(this::toDto).collect(Collectors.toList());
}
    }

    private final DummyMapper mapper = new DummyMapper();

    @Test
    void testToEntity() {
        DummyDto dto = new DummyDto("value1");
        DummyEntity entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals("value1", entity.value);
    }

    @Test
    void testToDto() {
        DummyEntity entity = new DummyEntity("value2");
        DummyDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals("value2", dto.value);
    }

    @Test
    void testToEntityList() {
        List<DummyDto> dtos = Arrays.asList(new DummyDto("a"), new DummyDto("b"));
        List<DummyEntity> entities = mapper.toEntity(dtos);
        assertEquals(2, entities.size());
        assertEquals("a", entities.get(0).value);
        assertEquals("b", entities.get(1).value);
    }

    @Test
    void testToDtoList() {
        List<DummyEntity> entities = Arrays.asList(new DummyEntity("x"), new DummyEntity("y"));
        List<DummyDto> dtos = mapper.toDto(entities);
        assertEquals(2, dtos.size());
        assertEquals("x", dtos.get(0).value);
        assertEquals("y", dtos.get(1).value);
    }
}