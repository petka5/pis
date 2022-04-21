package org.petka.pis.controllers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.petka.pis.configuration.Role;
import org.petka.pis.model.OperatorPetRequest;
import org.petka.pis.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
@Import(OperatorPetsControllerTest.TestConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperatorPetsControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = new ObjectMapper();

    private String id;

    @TestConfiguration
    @RequiredArgsConstructor
    static class TestConfig {

        @Bean
        @Primary
        public AuditorAware<String> auditorAware() {
            return (AuditorAware) () -> Optional.of("Operator");
        }
    }

    @Test
    @Order(1)
    @SneakyThrows
    @WithMockUser(roles = {Role.Names.OPERATOR})
    @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Need for the tests.")
    void createPet() {
        OperatorPetRequest request = new OperatorPetRequest();
        request.name("name");
        request.kind("kind");
        request.type(Type.DOMESTIC);
        request.age(1L);
        request.orgId(UUID.randomUUID());

        MvcResult response = mockMvc.perform(post(Paths.OPERATOR_PETS).contentType(MediaType.APPLICATION_JSON)
                                                     .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        String content = response.getResponse().getContentAsString();
        id = JsonPath.read(content, "$.id");
    }

    @Test
    @Order(2)
    @WithMockUser(roles = {Role.Names.OPERATOR})
    void getPetById() throws Exception {

        mockMvc.perform(get(Paths.OPERATOR_PETS_ID, id)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.orgId").exists())
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.createdBy", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedBy", notNullValue()))
                .andExpect(jsonPath("$.kind", is("kind")))
                .andExpect(jsonPath("$.age", is(1)))
                .andExpect(jsonPath("$.version", notNullValue(), Long.class))
                .andExpect(jsonPath("$.createDateTime", notNullValue()))
                .andExpect(jsonPath("$.updateDateTime", notNullValue()))
                .andExpect(jsonPath("$.deleted", is(false)));
    }

    @Test
    @WithMockUser(roles = {Role.Names.OPERATOR})
    void getPetByIdNotFound() throws Exception {
        mockMvc.perform(get(Paths.OPERATOR_PETS_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {Role.Names.OPERATOR})
    void getPets() throws Exception {
        this.mockMvc.perform(get(Paths.OPERATOR_PETS)).andExpect(status().is2xxSuccessful());
    }
}
