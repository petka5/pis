package org.petka.pis.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.persistence.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PetServiceTest {

    @Autowired
    private PetService petService;

    @MockBean
    private PetRepository repository;

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdUpdated = OffsetDateTime.now();
        Pet pet = Pet.builder().name("name").kind("kind").age(1)
                .id(id).deleted(false).version(1)
                .createDateTime(createdUpdated).updateDateTime(createdUpdated).build();

        doReturn(Optional.of(pet)).when(repository).findById(id);
        Optional<Pet> findById = petService.findById(id);
        assertTrue(findById.isPresent());
        assertSame(pet, findById.get());
    }
}
