package org.petka.pis.controllers;

import javax.validation.Valid;

import org.petka.pis.api.PetsApi;
import org.petka.pis.mapper.PetsMapper;
import org.petka.pis.model.NewPet;
import org.petka.pis.model.Pet;
import org.petka.pis.servicies.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements PetsApi {

    private final PetsMapper petsMapper;
    private final PetService petService;

    @Autowired
    public TestController(final PetsMapper petsMapper, final PetService petService) {
        this.petsMapper = petsMapper;
        this.petService = petService;
    }


    @Override
    public ResponseEntity<Pet> addPet(@Valid final NewPet pet) {
        return new ResponseEntity<>(petsMapper.entityToDto(petService.create(petsMapper.dtoToEntity(pet))),
                                    HttpStatus.CREATED);
    }
}
