package org.petka.pis.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.delegators.OperatorPetsDelegator;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class OperatorPetsDelegateTest {

    @InjectMocks
    private OperatorPetsDelegator operatorPetsDelegator;

    @Mock
    private PetService petService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PatchComponent patchComponent;

    @Test
    @SneakyThrows
    void testUpdatePet() {
        when(petService.findById(any(UUID.class))).thenReturn(Optional.of(Pet.builder().build()));
        when(petService.update(any(Pet.class))).then(returnsFirstArg());
        when(patchComponent.patch(any(), any())).then(returnsFirstArg());
        when(modelMapper.map(any(), any())).thenReturn(new PetResponse());

        ResponseEntity<PetResponse> response = operatorPetsDelegator.operatorUpdatePet(UUID.randomUUID(), new Object());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        InOrder inOrder = inOrder(petService, patchComponent, modelMapper);
        inOrder.verify(petService).findById(any(UUID.class));
        inOrder.verify(patchComponent).patch(any(), any());
        inOrder.verify(petService).update(any(Pet.class));
        inOrder.verify(modelMapper).map(any(), any());

        inOrder.verifyNoMoreInteractions();
    }
}
