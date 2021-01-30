package org.petka.pis.delegators;

import java.util.List;

import javax.validation.Valid;

import org.petka.pis.api.PetsApiDelegate;
import org.petka.pis.mapper.PetsMapper;
import org.petka.pis.model.NewPet;
import org.petka.pis.model.Pet;
import org.petka.pis.servicies.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetsDelegator implements PetsApiDelegate {

    private final PetsMapper petsMapper;
    private final PetService petService;

    @Autowired
    public PetsDelegator(final PetsMapper petsMapper, final PetService petService) {
        this.petsMapper = petsMapper;
        this.petService = petService;
    }


    @Override
    public ResponseEntity<Pet> addPet(@Valid final NewPet pet) {
        return new ResponseEntity<>(petsMapper.entityToDto(petService.create(petsMapper.dtoToEntity(pet))),
                                    HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<Pet>> findPets(final List<String> tags, final Integer limit) {
        log.info("Getting all pets");
        return new ResponseEntity<>(petsMapper.entitiesToDtos(petService.findPets()), HttpStatus.OK);
    }
}
