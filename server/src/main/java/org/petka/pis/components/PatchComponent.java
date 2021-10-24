package org.petka.pis.components;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.petka.pis.persistence.entities.BaseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Component that performs simple patch operation.
 */
@Component
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Object mapper could be changed.")
public class PatchComponent {

    private final ObjectMapper objectMapper;

    private List<String> fieldToBeOmittedOnUpdate;

    /**
     * Initialize {@link PatchComponent#fieldToBeOmittedOnUpdate}.
     */
    @PostConstruct
    public void getFields() {
        fieldToBeOmittedOnUpdate = List.of(Arrays.stream(BaseEntity.class.getDeclaredFields())
                                                   .map(Field::getName).toArray(String[]::new));
    }

    /**
     * Patch entity.
     *
     * @param entity entity
     * @param patch  new values
     * @param <T>    type of the patched element
     * @return patched entity
     */
    @SneakyThrows
    public <T> T patch(final T entity, final Object patch) {
        ObjectReader objectReader = objectMapper.readerForUpdating(entity);
        JsonNode jsonNode = objectMapper.convertValue(patch, JsonNode.class);
        fieldToBeOmittedOnUpdate.forEach(((ObjectNode) jsonNode)::remove);
        return objectReader.readValue(jsonNode);
    }
}
