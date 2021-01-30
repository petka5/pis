package org.petka.pis.controllers.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.persistence.repositories.PetsRepository;
import org.petka.pis.servicies.PetService;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetsRepository petsRepository;

    private PetService petService;

    @BeforeEach
    void init() {
        petService = new PetService(petsRepository);
    }

    @Test
    @DisplayName("Testing findPets method")
    void test() {
        List<Pet> pets = petService.findPets();
        Assertions.assertNotNull(pets);
    }
}
