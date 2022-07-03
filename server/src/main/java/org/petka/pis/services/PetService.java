package org.petka.pis.services;

import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.persistence.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService extends OrgBaseService<Pet> {

    @Autowired
    public PetService(final PetRepository repository) {
        super(repository);
    }
}

