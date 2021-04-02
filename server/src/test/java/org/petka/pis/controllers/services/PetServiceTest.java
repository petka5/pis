package org.petka.pis.controllers.services;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.persistence.repositories.PetRepository;
import org.petka.pis.persistence.restquery.RestQuery;
import org.petka.pis.persistence.restquery.SearchCriteria;
import org.petka.pis.persistence.restquery.SearchOperation;
import org.petka.pis.services.PetService;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository baseRepository;

    private PetService petService;

    @BeforeEach
    void init() {
        petService = new PetService(baseRepository);
    }

    @Test
    @DisplayName("Testing findPets method")
    void test() {
        RestQuery restQuery = new RestQuery();
        restQuery.add(SearchCriteria.builder().key("name").operation(SearchOperation.EQUALITY).value("pet1").build());
        Page<Pet> search = petService.search(restQuery);
        assertNull(search);
    }
}
