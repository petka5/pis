package org.petka.pis.delegators;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.api.PetsApiDelegate;
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetRequest;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.persistence.restquery.RequestQueryFilter;
import org.petka.pis.servicies.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetsDelegator implements PetsApiDelegate {

    private final PetService petService;
    private final RequestQueryFilter requestQueryFilter;

    private final ModelMapper modelMapper;


    @Autowired
    public PetsDelegator(final PetService petService, final RequestQueryFilter requestQueryFilter,
                         final ModelMapper modelMapper) {
        this.petService = petService;
        this.requestQueryFilter = requestQueryFilter;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<PetResponse> addPet(final PetRequest pet) {
        log.info("Creating pet");
        return new ResponseEntity<>(
                modelMapper.map(petService.create(modelMapper.map(pet, Pet.class)), PetResponse.class),
                HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<PetPageResponse> findPets(final @SuppressWarnings("unused") Integer page,
                                                    final @SuppressWarnings("unused") Integer size,
                                                    final @SuppressWarnings("unused") String sort,
                                                    final @SuppressWarnings("unused") List<String> filter,
                                                    final @DefaultValue("false") Boolean includeDeleted) {
        log.info("Getting all pets");
        PetPageResponse response =
                modelMapper.map(petService.search(requestQueryFilter.getRequestQuery(), includeDeleted),
                                PetPageResponse.class);
        log.info("Founded pets {}", response.getNumber());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PetResponse> findPetById(final UUID id) {
        log.info("Getting pet {}", id);
        return petService.findById(id).map(e -> modelMapper.map(e, PetResponse.class))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Void> deletePet(final UUID id) {
        log.info("Deleting pet {}", id);
        return petService.deleteById(id)
                .map(e -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
