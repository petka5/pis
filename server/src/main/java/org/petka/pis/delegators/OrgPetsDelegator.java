package org.petka.pis.delegators;

import java.util.UUID;

import org.petka.pis.api.OrgIdApiDelegate;
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetRequest;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "OperatorBaseDelegate")
public class OrgPetsDelegator implements OrgIdApiDelegate {

    private final OrgBaseDelegate<Pet, PetResponse, PetPageResponse> orgBaseDelegate;

    @Override
    public ResponseEntity<PetResponse> orgAddPet(final UUID orgId, final PetRequest petRequest) {
        log.info("Creating pet for org {}", orgId);

        return orgBaseDelegate.orgAdd(orgId, petRequest, Pet.class, PetResponse.class);
    }

    @Override
    public ResponseEntity<Void> orgDeletePet(final UUID orgId, final UUID id) {
        log.info("Deleting pet {} from org {}", id, orgId);
        return orgBaseDelegate.orgDelete(orgId, id);
    }

    @Override
    public ResponseEntity<PetResponse> orgFindPetById(final UUID orgId, final UUID id) {
        log.info("Getting pet {} from org {}", id, orgId);
        return orgBaseDelegate.orgFindById(orgId, id, PetResponse.class);
    }

    @Override
    public ResponseEntity<PetPageResponse> orgFindPets(final UUID orgId, final Integer page, final Integer size,
                                                       final String sort,
                                                       final String filter, final Boolean includeDeleted,
                                                       final Boolean includeCount) {

        log.info("Getting all pets for org {}", orgId);
        return orgBaseDelegate.orgFind(orgId, page, size, sort, filter, includeDeleted, includeCount,
                                       PetPageResponse.class);
    }

    @Override
    public ResponseEntity<PetResponse> orgUpdatePet(final UUID orgId, final UUID id, final Object body) {
        log.info("Updating pet {} from org {}", id, orgId);
        return orgBaseDelegate.orgUpdate(orgId, id, body, PetResponse.class);
    }
}
