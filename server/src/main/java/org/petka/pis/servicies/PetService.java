package org.petka.pis.servicies;

import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.persistence.repositories.PetsRepository;
import org.petka.pis.persistence.restquery.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    private final PetsRepository petsRepository;

    @Autowired
    public PetService(final PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    /**
     * Creates new Pet in the database.
     *
     * @param pet new entity.
     * @return stored entity.
     */
    public Pet create(final Pet pet) {
        return petsRepository.save(pet);
    }

    /**
     * Search database by rest query.
     *
     * @param query RestQuery
     * @return Page of pets
     */
    public Page<Pet> search(final Query query) {
        return petsRepository.search(query);
    }
}

