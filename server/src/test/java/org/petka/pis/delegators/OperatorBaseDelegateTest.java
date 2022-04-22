package org.petka.pis.delegators;

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
import org.petka.pis.model.PetPageResponse;
import org.petka.pis.model.PetResponse;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class OperatorBaseDelegateTest {

    @InjectMocks
    private BaseDelegate<Pet, PetResponse, PetPageResponse> baseDelegate;

    @Mock
    private BaseService<Pet> baseService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PatchComponent patchComponent;


    @Test
    void testUpdateById() {
        when(baseService.findById(any(UUID.class))).thenReturn(Optional.of(new Pet()));
        when(patchComponent.patch(any(), any())).then(returnsFirstArg());
        when(baseService.update(any(Pet.class))).then(returnsFirstArg());
        when(modelMapper.map(any(), any())).thenReturn(new Pet());

        ResponseEntity<PetResponse> response =
                baseDelegate.updateById(UUID.randomUUID(), new Pet(), PetResponse.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        InOrder inOrder = inOrder(baseService, modelMapper, patchComponent);
        inOrder.verify(baseService).findById(any());
        inOrder.verify(patchComponent).patch(any(), any());
        inOrder.verify(baseService).update(any());
        inOrder.verify(modelMapper).map(any(), any());

        inOrder.verifyNoMoreInteractions();
    }
}
