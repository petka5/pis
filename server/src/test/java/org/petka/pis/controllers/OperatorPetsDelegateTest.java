package org.petka.pis.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.petka.pis.delegators.BaseDelegate;
import org.petka.pis.delegators.OperatorPetsDelegator;
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class OperatorPetsDelegateTest {

    @InjectMocks
    private OperatorPetsDelegator operatorPetsDelegator;

    @Mock
    private BaseDelegate<Pet, PetResponse, PetPageResponse> baseDelegate;

    @Test
    @SneakyThrows
    void testUpdatePet() {

        when(baseDelegate.updateById(any(UUID.class), any(Object.class), eq(PetResponse.class))).thenReturn(
                new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<PetResponse> response = operatorPetsDelegator.operatorUpdatePet(UUID.randomUUID(), new Object());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        InOrder inOrder = inOrder(baseDelegate);
        inOrder.verify(baseDelegate).updateById(any(), any(), any());

        inOrder.verifyNoMoreInteractions();
    }
}
