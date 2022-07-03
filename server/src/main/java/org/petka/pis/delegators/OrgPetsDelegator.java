package org.petka.pis.delegators;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.api.OrganizationPetsApiDelegate;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetRequest;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Service
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "BaseDelegate")
public class OrgPetsDelegator extends OrgBaseDelegate<Pet, PetResponse, PetPageResponse> implements
                                                                                         OrganizationPetsApiDelegate {

    public OrgPetsDelegator(final ModelMapper modelMapper, final PetService petService,
                            final SpecificationComponent specificationComponent,
                            final PatchComponent patchComponent) {
        super(modelMapper, petService, specificationComponent, patchComponent);
    }

    @Override
    public ResponseEntity<PetResponse> orgAddPet(final UUID orgId, final PetRequest petRequest) {
        return orgCreate(orgId, petRequest, Pet.class, PetResponse.class);
    }

    @Override
    public ResponseEntity<Void> orgDeletePet(final UUID orgId, final UUID id) {
        return orgDeleteById(orgId, id);
    }

    @Override
    public ResponseEntity<PetResponse> orgFindPetById(final UUID orgId, final UUID id) {
        return orgFindById(orgId, id, PetResponse.class);
    }

    @Override
    public ResponseEntity<PetPageResponse> orgFindPets(final UUID orgId, final Integer page, final Integer size,
                                                       final String sort,
                                                       final String filter, final Boolean includeDeleted,
                                                       final Boolean includeCount) {

        return orgFindAll(orgId, page, size, sort, filter, includeDeleted, includeCount,
                          PetPageResponse.class);
    }

    @Override
    public ResponseEntity<PetResponse> orgUpdatePet(final UUID orgId, final UUID id, final Object body) {
        return orgUpdateById(orgId, id, body, PetResponse.class);
    }
}
