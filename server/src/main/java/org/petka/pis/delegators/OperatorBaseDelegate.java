package org.petka.pis.delegators;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.services.BaseService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;

/**
 * OperatorBaseDelegate performs operator operations.
 *
 * @param <E> entity
 * @param <R> response type
 * @param <P> page response type
 */
@Service
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Model mapper could be changed.")
public class OperatorBaseDelegate<E extends BaseEntity, R, P> {

    private final ModelMapper modelMapper;
    private final BaseService<E> baseService;
    private final SpecificationComponent specificationComponent;
    private final PatchComponent patchComponent;


    /**
     * Creates entity.
     *
     * @param request      request entity
     * @param entityType   entity type
     * @param responseType response type
     * @return entity created entity
     */
    public ResponseEntity<R> add(final Object request, final Class<E> entityType, final Class<R> responseType) {

        return Optional.of(modelMapper.map(request, entityType))
                .map(baseService::create)
                .map(e -> modelMapper.map(e, responseType))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseGet(ResponseEntity.internalServerError()::build);
    }

    /**
     * Find entity by given filter.
     *
     * @param page           page number
     * @param size           page size
     * @param sort           sort
     * @param filter         filter
     * @param includeDeleted include deleted entities on the result
     * @param includeCount   include count in the response
     * @param responseType   response type
     * @return Page with entities
     */
    public ResponseEntity<P> find(final Integer page, final Integer size, final String sort, final String filter,
                                  final @DefaultValue("false") Boolean includeDeleted,
                                  final @DefaultValue("false") Boolean includeCount,
                                  final Class<P> responseType) {

        return Optional.of(baseService.findAll(specificationComponent.createSpecification(filter),
                                               specificationComponent.createPageRequest(page, size, sort),
                                               includeDeleted, includeCount))
                .map(e -> modelMapper.map(e, responseType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.internalServerError()::build);
    }

    /**
     * Find by id.
     *
     * @param id           entity id
     * @param responseType response type
     * @return entity
     */
    public ResponseEntity<R> findById(final UUID id, final Class<R> responseType) {

        return baseService.findById(id)
                .map(e -> modelMapper.map(e, responseType))
                .map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }

    /**
     * Delete entity by id.
     *
     * @param id id
     * @return no content
     */
    public ResponseEntity<Void> deleteById(final UUID id) {

        return baseService.deleteById(id)
                .map(e -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                .orElseGet(ResponseEntity.notFound()::build);
    }

    /**
     * Update entity by id.
     *
     * @param id           id
     * @param body         patch request
     * @param responseType response type
     * @return updated entity
     */
    public ResponseEntity<R> updateById(final UUID id, final Object body, final Class<R> responseType) {

        return baseService.findById(id)
                .map(e -> patchComponent.patch(e, body))
                .map(baseService::update)
                .map(e -> modelMapper.map(e, responseType))
                .map(ResponseEntity::ok).orElseGet(ResponseEntity.notFound()::build);
    }
}
