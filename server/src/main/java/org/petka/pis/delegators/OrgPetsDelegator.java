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

@Service
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "BaseDelegate")
public class OrgPetsDelegator implements OrgIdApiDelegate {

    private final BaseDelegate<Pet, PetResponse, PetPageResponse> baseDelegate;

    @Override
    public ResponseEntity<PetResponse> orgAddPet(final UUID orgId, final PetRequest petRequest) {
        return baseDelegate.orgCreate(orgId, petRequest, Pet.class, PetResponse.class);
    }

    @Override
    public ResponseEntity<Void> orgDeletePet(final UUID orgId, final UUID id) {
        return baseDelegate.orgDeleteById(orgId, id);
    }

    @Override
    public ResponseEntity<PetResponse> orgFindPetById(final UUID orgId, final UUID id) {
        return baseDelegate.orgFindById(orgId, id, PetResponse.class);
    }

    @Override
    public ResponseEntity<PetPageResponse> orgFindPets(final UUID orgId, final Integer page, final Integer size,
                                                       final String sort,
                                                       final String filter, final Boolean includeDeleted,
                                                       final Boolean includeCount) {

        return baseDelegate.orgFindAll(orgId, page, size, sort, filter, includeDeleted, includeCount,
                                       PetPageResponse.class);
    }

    @Override
    public ResponseEntity<PetResponse> orgUpdatePet(final UUID orgId, final UUID id, final Object body) {
        return baseDelegate.orgUpdateById(orgId, id, body, PetResponse.class);
    }
}
