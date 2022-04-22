package org.petka.pis.delegators;

import java.util.UUID;

import org.petka.pis.api.OperatorApiDelegate;
import org.petka.pis.model.OperatorPetRequest;
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "OperatorBaseDelegate")
public class OperatorPetsDelegator implements OperatorApiDelegate {

    private final BaseDelegate<Pet, PetResponse, PetPageResponse> baseDelegate;


    @Override
    public ResponseEntity<PetResponse> operatorAddPet(final OperatorPetRequest pet) {
        return baseDelegate.create(pet, Pet.class, PetResponse.class);
    }


    @Override
    public ResponseEntity<PetPageResponse> operatorFindPets(final Integer page, final Integer size, final String sort,
                                                            final String filter,
                                                            final @DefaultValue("false") Boolean includeDeleted,
                                                            final @DefaultValue("false") Boolean includeCount) {
        return baseDelegate.findAll(page, size, sort, filter, includeDeleted, includeCount, PetPageResponse.class);
    }

    @Override
    public ResponseEntity<PetResponse> operatorFindPetById(final UUID id) {
        return baseDelegate.findById(id, PetResponse.class);
    }

    @Override
    public ResponseEntity<Void> operatorDeletePet(final UUID id) {
        return baseDelegate.deleteById(id);
    }

    @Override
    public ResponseEntity<PetResponse> operatorUpdatePet(final UUID id, final Object body) {
        return baseDelegate.updateById(id, body, PetResponse.class);
    }
}
