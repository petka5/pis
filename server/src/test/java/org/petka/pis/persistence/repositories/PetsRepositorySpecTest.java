package org.petka.pis.persistence.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.persistence.restquery.Query;
import org.petka.pis.persistence.restquery.SearchCriteria;
import org.petka.pis.persistence.restquery.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;


//https://www.baeldung.com/rest-api-search-language-spring-data-specifications
//https://www.baeldung.com/rest-api-query-search-language-more-operations
//https://stackoverflow.com/
//           questions/51127468/how-to-easy-implement-rest-api-query-language-with-querydsl-and-spring-data-to
//https://www.baeldung.com/rest-api-query-search-or-operation
@SpringBootTest
class PetsRepositorySpecTest {

    @Autowired
    private PetsRepository petsRepository;

    @BeforeEach
    public void init() {
        Pet pet1 = new Pet();
        pet1.setName("pet1");
        petsRepository.save(pet1);

        Pet pet2 = new Pet();
        pet2.setName("pet2");
        petsRepository.save(pet2);
    }

    @Test
    void testRepo() {
        Query query = new Query();
        query.add(SearchCriteria.builder().key("name").operation(SearchOperation.EQUALITY).value("pet1").build());
        Page<Pet> search = petsRepository.search(query);
        Assertions.assertNotNull(search);
    }
}
