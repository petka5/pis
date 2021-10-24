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
import org.petka.pis.delegators.PetsDelegator;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@ExtendWith(MockitoExtension.class)
class PetsDelegateTest {

    @InjectMocks
    private PetsDelegator petsDelegator;
    @Mock
    private PetService petService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PatchComponent patchComponent;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @SneakyThrows
    void testUpdatePet() {

        when(petService.findById(any())).thenReturn(Optional.of(new Pet()));
        when(patchComponent.patch(any(), any())).then(returnsFirstArg());
        when(petService.update(any())).then(returnsFirstArg());
        when(modelMapper.map(any(), any())).thenReturn(new PetResponse());

        ResponseEntity<PetResponse> response =
                petsDelegator.updatePet(UUID.randomUUID(), mapper.readTree("{\"age\":12}"));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        InOrder inOrder = inOrder(petService, modelMapper, patchComponent);
        inOrder.verify(petService).findById(any());
        inOrder.verify(patchComponent).patch(any(), any());
        inOrder.verify(petService).update(any());
        inOrder.verify(modelMapper).map(any(), any());

        inOrder.verifyNoMoreInteractions();
    }
}
