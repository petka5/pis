package org.petka.pis.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.persistence.entities.Pet;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PatchComponentTest {


    @Test
    @DisplayName("Testing BaseEntity patch method.")
    void testBaseEntityPatch() {
        BaseEntity build = BaseEntity.builder().id(UUID.randomUUID()).deleted(false).build();

        PatchComponent patchComponent = new PatchComponent(new ObjectMapper(),
                                                           List.of(Arrays.stream(BaseEntity.class.getDeclaredFields())
                                                                           .map(Field::getName)
                                                                           .toArray(String[]::new)));

        BaseEntity patch = patchComponent.patch(build, Map.of("id", UUID.randomUUID(), "deleted", true));

        assertNotNull(patch);
        assertEquals(build.getId(), patch.getId());
        assertEquals(build.isDeleted(), patch.isDeleted());
    }

    @ParameterizedTest
    @MethodSource("providePatchComponent")
    @DisplayName("Testing PetEntity patch method.")
    void testPetEntityPatch(final PatchComponent patchComponent) {

        Pet pet = Pet.builder().name("name").age(12).id(UUID.randomUUID())
                .version(1).kind("cat").type(Pet.Type.DOMESTIC)
                .build();
        Pet patch = patchComponent.patch(pet, Map.of("id", UUID.randomUUID(), "name", "name1", "type",
                                                     Pet.Type.WILD.name()));
        assertNotNull(patch);
        assertEquals(pet.getId(), patch.getId());
        assertEquals("name1", patch.getName());
        assertEquals(Pet.Type.WILD, patch.getType());
        assertEquals(pet.getKind(), patch.getKind());
    }

    private static Stream<Arguments> providePatchComponent() {
        return Stream.of(
                Arguments.of(new PatchComponent(new ObjectMapper(),
                                                List.of(Arrays.stream(BaseEntity.class.getDeclaredFields())
                                                                .map(Field::getName).toArray(String[]::new)))),
                Arguments.of(new PatchComponent(new ObjectMapper(), null))
        );
    }
}
