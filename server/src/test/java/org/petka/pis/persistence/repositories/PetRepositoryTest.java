package org.petka.pis.persistence.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.persistence.entities.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;


//https://www.baeldung.com/rest-api-search-language-spring-data-specifications
//https://www.baeldung.com/rest-api-query-search-language-more-operations
//https://stackoverflow.com/
//           questions/51127468/how-to-easy-implement-rest-api-query-language-with-querydsl-and-spring-data-to
//https://www.baeldung.com/rest-api-query-search-or-operation
@SpringBootTest
class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private SpecificationComponent specificationComponent;

    @BeforeEach
    public void init() {
        petRepository.save(Pet.builder().name("pet1").build());
        petRepository.save(Pet.builder().name("pet2").build());
    }

    @SuppressWarnings("checkstyle:magicNumber")
    @Test
    void testRepo() {
        Slice<Pet> search = petRepository.findAll(specificationComponent.createSpecification("name:'pet1'"),
                                                  specificationComponent.createPageRequest(0, 10, null),
                                                  false, false);
        assertNotNull(search);
        assertEquals(1, search.getNumberOfElements());
    }
}
