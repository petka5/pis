package org.petka.pis.delegators;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.api.OperatorPetsApiDelegate;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.model.OperatorPetRequest;
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.PetService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Service
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "BaseDelegate")
public class OperatorPetsDelegator extends OrgBaseDelegate<Pet, PetResponse, PetPageResponse>
        implements OperatorPetsApiDelegate {


    public OperatorPetsDelegator(final ModelMapper modelMapper,
                                 final PetService petService,
                                 final SpecificationComponent specificationComponent,
                                 final PatchComponent patchComponent) {
        super(modelMapper, petService, specificationComponent, patchComponent);
    }

    @Override
    public ResponseEntity<PetResponse> operatorAddPet(final OperatorPetRequest pet) {
        return create(pet, Pet.class, PetResponse.class);
    }


    @Override
    public ResponseEntity<PetPageResponse> operatorFindPets(final Integer page, final Integer size, final String sort,
                                                            final String filter,
                                                            final @DefaultValue("false") Boolean includeDeleted,
                                                            final @DefaultValue("false") Boolean includeCount) {
        return findAll(page, size, sort, filter, includeDeleted, includeCount, PetPageResponse.class);
    }

    @Override
    public ResponseEntity<PetResponse> operatorFindPetById(final UUID id) {
        return findById(id, PetResponse.class);
    }

    @Override
    public ResponseEntity<Void> operatorDeletePet(final UUID id) {
        return deleteById(id);
    }

    @Override
    public ResponseEntity<PetResponse> operatorUpdatePet(final UUID id, final Object body) {
        return updateById(id, body, PetResponse.class);
    }
}
