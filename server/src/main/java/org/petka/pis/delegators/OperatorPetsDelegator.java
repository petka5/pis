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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "OperatorBaseDelegate")
public class OperatorPetsDelegator implements OperatorApiDelegate {

    private final OperatorBaseDelegate<Pet, PetResponse, PetPageResponse> operatorBaseDelegate;


    @Override
    public ResponseEntity<PetResponse> operatorAddPet(final OperatorPetRequest pet) {
        log.info("Creating pet");
        return operatorBaseDelegate.add(pet, Pet.class, PetResponse.class);
    }


    @Override
    public ResponseEntity<PetPageResponse> operatorFindPets(final Integer page, final Integer size, final String sort,
                                                            final String filter,
                                                            final @DefaultValue("false") Boolean includeDeleted,
                                                            final @DefaultValue("false") Boolean includeCount) {
        log.info("Getting all pets");
        return operatorBaseDelegate.find(page, size, sort, filter, includeDeleted, includeCount, PetPageResponse.class);
    }

    @Override
    public ResponseEntity<PetResponse> operatorFindPetById(final UUID id) {
        log.info("Getting pet {}", id);
        return operatorBaseDelegate.findById(id, PetResponse.class);
    }

    @Override
    public ResponseEntity<Void> operatorDeletePet(final UUID id) {
        log.info("Deleting pet {}", id);
        return operatorBaseDelegate.deleteById(id);
    }

    @Override
    public ResponseEntity<PetResponse> operatorUpdatePet(final UUID id, final Object body) {
        log.info("Updating pet {}", id);
        return operatorBaseDelegate.updateById(id, body, PetResponse.class);
    }
}
