package org.petka.pis.components;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

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

    @Qualifier("nonPatchableFields")
    private final List<String> nonPatchableFields;

    /**
     * Patch entity.
     *
     * @param entity entity
     * @param patch  new values
     * @param <T>    type of the patched element
     * @return patched entity
     */
    @SneakyThrows(IOException.class)
    public <T> T patch(final T entity, final Object patch) {
        JsonNode jsonNode = objectMapper.convertValue(patch, JsonNode.class);

        List<String> fieldsList = Optional.ofNullable(nonPatchableFields)
                .stream().flatMap(Collection::stream)
                .filter(jsonNode::has)
                .toList();

        if (Objects.nonNull(fieldsList) && !CollectionUtils.isEmpty(fieldsList)) {
            throw new IllegalArgumentException(
                    "Cannot update fields: " + String.join(",", fieldsList));
        }

        ObjectReader objectReader = objectMapper.readerForUpdating(entity);
        return objectReader.readValue(jsonNode);
    }
}
