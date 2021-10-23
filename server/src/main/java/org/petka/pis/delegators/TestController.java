package org.petka.pis.delegators;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.petka.pis.persistence.entities.Pet;
import org.petka.pis.services.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class TestController {

    private final PetService petService;
    private final ObjectMapper objectMapper;

    @PatchMapping(path = "/test1/{id}")
    public ResponseEntity<Pet> updatePet1(final @PathVariable UUID id, @RequestBody final JsonNode object)
            throws IOException {
        Optional<Pet> pet = petService.findById(id);
        ObjectReader updater = objectMapper.readerForUpdating(pet.get());
        Pet merged = updater.readValue(object);
        petService.update(merged);

        return ResponseEntity.ok(merged);
    }


    //https://stackoverflow.com/questions/45635607/spring-data-rest-patch-forbid-update-on-specific-fields
    //@PatchMapping(path = "/test/{id}", consumes = "application/merge-patch+json")
    @PatchMapping(path = "/test/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Pet> updatePet(final @PathVariable UUID id, @RequestBody final JsonPatch patch)
            throws JsonPatchException, JsonProcessingException {

        Optional<Pet> pet = petService.findById(id);

        Pet updatedPet = applyPatchToCustomer(patch, pet.get());
        petService.update(updatedPet);


/*        List<String> allowedFields = List.of("age");
        ObjectNode validPatch = patch.retain(allowedFields);
        JsonPatch jsonPatcher = new JsonPatch();
        Person patchedPerson = jsonPatcher.merge(validPatch, person);*/
        return ResponseEntity.ok(updatedPet);
        //return new ResponseEntity<>(new Pet(), HttpStatus.OK);
    }

    private Pet applyPatchToCustomer(
            JsonPatch patch, Pet targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCustomer, JsonNode.class));
        return objectMapper.treeToValue(patched, Pet.class);
    }
}
