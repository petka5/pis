package org.petka.pis.persistence.repositories;

import java.util.UUID;

import org.petka.pis.persistence.entities.Pet;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends OrgCustomRepository<Pet, UUID> {

}
