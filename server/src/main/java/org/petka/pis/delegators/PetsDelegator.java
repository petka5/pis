package org.petka.pis.delegators;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.api.PetsApiDelegate;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetRequest;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.PetService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Model mapper could be changed.")
public class PetsDelegator implements PetsApiDelegate {

    private final PetService petService;
    private final SpecificationComponent specificationComponent;
    private final ModelMapper modelMapper;
    private final PatchComponent patchComponent;


    @Override
    public ResponseEntity<PetResponse> addPet(final PetRequest pet) {
        log.info("Creating pet");

        return Optional.of(modelMapper.map(pet, Pet.class))
                .map(petService::create)
                .map(e -> modelMapper.map(e, PetResponse.class))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseGet(ResponseEntity.internalServerError()::build);
    }


    @Override
    public ResponseEntity<PetPageResponse> findPets(final Integer page, final Integer size, final String sort,
                                                    final String filter,
                                                    final @DefaultValue("false") Boolean includeDeleted,
                                                    final @DefaultValue("false") Boolean includeCount) {
        log.info("Getting all pets");

        return Optional.of(petService.findAll(specificationComponent.createSpecification(filter),
                                              specificationComponent.createPageRequest(page, size, sort),
                                              includeDeleted, includeCount))
                .map(e -> modelMapper.map(e, PetPageResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.internalServerError()::build);
    }

    @Override
    public ResponseEntity<PetResponse> findPetById(final UUID id) {
        log.info("Getting pet {}", id);
        return petService.findById(id)
                .map(e -> modelMapper.map(e, PetResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<Void> deletePet(final UUID id) {
        log.info("Deleting pet {}", id);
        return petService.deleteById(id)
                .map(e -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<PetResponse> updatePet(final UUID id, final Object body) {
        log.info("Updating pet {}", id);
        return petService.findById(id)
                .map(e -> patchComponent.patch(e, body))
                .map(petService::update)
                .map(e -> modelMapper.map(e, PetResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
