package org.petka.pis.persistence.repositories;

import org.petka.pis.persistence.entities.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsRepository extends CrudRepository<Pet, Long> {

}
