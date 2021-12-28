package org.petka.pis.delegators;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.api.OrgIdApiDelegate;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetRequest;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.PetService;
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
public class OrgPetsDelegator implements OrgIdApiDelegate {

    private final PetService petService;
    private final SpecificationComponent specificationComponent;
    private final ModelMapper modelMapper;
    private final PatchComponent patchComponent;


    @Override
    public ResponseEntity<PetResponse> orgAddPet(final UUID orgId, final PetRequest petRequest) {
        log.info("Creating pet for org {}", orgId);

        return Optional.of(modelMapper.map(petRequest, Pet.class))
                .map(e -> e.toBuilder().orgId(orgId).build())
                .map(petService::create)
                .map(e -> modelMapper.map(e, PetResponse.class))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseGet(ResponseEntity.internalServerError()::build);
    }

    @Override
    public ResponseEntity<Void> orgDeletePet(final UUID orgId, final UUID id) {
        log.info("Deleting pet {} from org {}", id, orgId);
        return petService.deleteByIdAndOrgId(id, orgId)
                .map(e -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<PetResponse> orgFindPetById(final UUID orgId, final UUID id) {
        log.info("Getting pet {} from org {}", id, orgId);
        return petService.findByIdAndOrgId(id, orgId)
                .map(e -> modelMapper.map(e, PetResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @Override
    public ResponseEntity<PetPageResponse> orgFindPets(final UUID orgId, final Integer page, final Integer size,
                                                       final String sort,
                                                       final String filter, final Boolean includeDeleted,
                                                       final Boolean includeCount) {

        log.info("Getting all pets for org {}", orgId);

        return Optional.of(petService.findAll(specificationComponent.createSpecification(filter, orgId),
                                              specificationComponent.createPageRequest(page, size, sort),
                                              includeDeleted, includeCount))
                .map(e -> modelMapper.map(e, PetPageResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.internalServerError()::build);
    }

    @Override
    public ResponseEntity<PetResponse> orgUpdatePet(final UUID orgId, final UUID id, final Object body) {
        log.info("Updating pet {} from org {}", id, orgId);
        return petService.findByIdAndOrgId(id, orgId)
                .map(e -> patchComponent.patch(e, body))
                .map(petService::update)
                .map(e -> modelMapper.map(e, PetResponse.class))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
