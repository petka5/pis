package org.petka.pis.components;

import java.util.List;
import java.util.Optional;

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

    private final List<String> fieldToBeOmittedOnUpdate;

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

        Optional.ofNullable(fieldToBeOmittedOnUpdate).ifPresent(((ObjectNode) jsonNode)::remove);

        return objectReader.readValue(jsonNode);
    }
}
