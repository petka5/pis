package org.petka.pis.controllers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.petka.pis.configuration.Role;
import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class OperatorPetsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Test
    @WithMockUser(roles = {Role.Names.OPERATOR})
    void getPetById() throws Exception {

        OffsetDateTime createdUpdated = OffsetDateTime.now();
        Pet pet = Pet.builder().name("name").kind("kind").age(1).id(UUID.randomUUID()).orgId(UUID.randomUUID())
                .createdBy("petka").lastModifiedBy("petka").deleted(false).version(1).createDateTime(createdUpdated)
                .updateDateTime(createdUpdated).build();

        doReturn(Optional.of(pet)).when(petService).findById(pet.getId());
        mockMvc.perform(get(Paths.OPERATOR_PETS_ID, pet.getId().toString())).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(pet.getId().toString())))
                .andExpect(jsonPath("$.orgId", is(pet.getOrgId().toString())))
                .andExpect(jsonPath("$.name", is(pet.getName())))
                .andExpect(jsonPath("$.createdBy", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedBy", notNullValue()))
                .andExpect(jsonPath("$.kind", is(pet.getKind())))
                .andExpect(jsonPath("$.age", is(pet.getAge())))
                .andExpect(jsonPath("$.version", is(pet.getVersion()), Long.class))
                .andExpect(jsonPath("$.createDateTime", is(pet.getCreateDateTime().toString())))
                .andExpect(jsonPath("$.updateDateTime", is(pet.getUpdateDateTime().toString())))
                .andExpect(jsonPath("$.deleted", is(pet.isDeleted())));
    }

    @Test
    @WithMockUser(roles = {Role.Names.OPERATOR})
    void getPetByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doReturn(Optional.empty()).when(petService).findById(id);
        mockMvc.perform(get(Paths.OPERATOR_PETS_ID, id.toString())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {Role.Names.OPERATOR})
    void testGetPets() throws Exception {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdUpdated = OffsetDateTime.now();
        Pet pet = Pet.builder().name("name").kind("kind").age(1).id(id).deleted(false).version(1)
                .createDateTime(createdUpdated).updateDateTime(createdUpdated).build();

        doReturn(new PageImpl<>(List.of(pet))).when(petService).findAll(any(), any(), anyBoolean(), anyBoolean());
        this.mockMvc.perform(get(Paths.OPERATOR_PETS)).andExpect(status().is2xxSuccessful());
    }
}
