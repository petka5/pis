package org.petka.pis.controllers.services;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.persistence.repositories.PetsRepository;
import org.petka.pis.persistence.restquery.Query;
import org.petka.pis.persistence.restquery.SearchCriteria;
import org.petka.pis.persistence.restquery.SearchOperation;
import org.petka.pis.servicies.PetService;
import org.springframework.data.domain.Page;

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
        Query query = new Query();
        query.add(SearchCriteria.builder().key("name").operation(SearchOperation.EQUALITY).value("pet1").build());
        Page<Pet> search = petService.search(query);
        assertNull(search);
    }
}
