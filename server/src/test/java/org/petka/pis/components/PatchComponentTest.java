package org.petka.pis.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.persistence.entities.Pet;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PatchComponentTest {

    private PatchComponent patchComponent;

    @BeforeEach
    void init() {
        patchComponent = new PatchComponent(new ObjectMapper(),
                                            List.of(Arrays.stream(BaseEntity.class.getDeclaredFields())
                                                            .map(Field::getName).toArray(String[]::new)));
    }

    @Test
    @DisplayName("Testing patching of non patchable fields.")
    void testBaseEntityPatch() {
        BaseEntity build = BaseEntity.builder().id(UUID.randomUUID()).build();
        Map<String, UUID> body = Map.of("id", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> patchComponent.patch(build, body));
    }

    @Test
    @DisplayName("Testing patching of entity.")
    void testPetEntityPatch() {
        Pet pet = Pet.builder().name("name").build();

        Pet patch = patchComponent.patch(pet, Map.of("name", "name1"));

        assertNotNull(patch);
        assertEquals("name1", patch.getName());
    }
}
