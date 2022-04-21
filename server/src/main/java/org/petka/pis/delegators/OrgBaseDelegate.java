package org.petka.pis.delegators;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.services.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;

/**
 * OrgBaseDelegate performs organization operations.
 *
 * @param <E> entity
 * @param <R> response type
 * @param <P> page response type
 */
@Service
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Model mapper could be changed.")
public class OrgBaseDelegate<E extends BaseEntity, R, P> {

    private final ModelMapper modelMapper;
    private final BaseService<E> baseService;
    private final SpecificationComponent specificationComponent;
    private final PatchComponent patchComponent;

    /**
     * Creates entity.
     *
     * @param orgId        org id
     * @param request      request
     * @param entityType   entity type
     * @param responseType response type
     * @return created entity
     */
    public ResponseEntity<R> orgAdd(final UUID orgId, final Object request, final Class<E> entityType,
                                    final Class<R> responseType) {

        return Optional.of(modelMapper.map(request, entityType))
                .map(e -> e.toBuilder().orgId(orgId).build())
                .map(entityType::cast)
                .map(baseService::create)
                .map(e -> modelMapper.map(e, responseType))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseGet(ResponseEntity.internalServerError()::build);
    }

    /**
     * Delete entity.
     *
     * @param orgId org id
     * @param id    id
     * @return no content
     */
    public ResponseEntity<Void> orgDelete(final UUID orgId, final UUID id) {

        return baseService.deleteByIdAndOrgId(id, orgId)
                .map(e -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                .orElseGet(ResponseEntity.notFound()::build);
    }

    /**
     * Get entity by id and org id.
     *
     * @param orgId        org id
     * @param id           id
     * @param responseType response type
     * @return entity
     */
    public ResponseEntity<R> orgFindById(final UUID orgId, final UUID id, final Class<R> responseType) {

        return baseService.findByIdAndOrgId(id, orgId)
                .map(e -> modelMapper.map(e, responseType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    /**
     * Find entities by given filter.
     *
     * @param orgId          org id
     * @param page           page
     * @param size           page size
     * @param sort           sort
     * @param filter         filter
     * @param includeDeleted include deleted
     * @param includeCount   include count
     * @param responseType   response type
     * @return Page of entities
     */
    public ResponseEntity<P> orgFind(final UUID orgId, final Integer page, final Integer size,
                                     final String sort,
                                     final String filter, final Boolean includeDeleted,
                                     final Boolean includeCount,
                                     final Class<P> responseType) {

        return Optional.of(baseService.findAll(specificationComponent.createSpecification(filter, orgId),
                                               specificationComponent.createPageRequest(page, size, sort),
                                               includeDeleted, includeCount))
                .map(e -> modelMapper.map(e, responseType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.internalServerError()::build);
    }


    /**
     * Update entity.
     *
     * @param orgId        org id
     * @param id           id
     * @param body         patch request
     * @param responseType response type
     * @return updated entity
     */
    public ResponseEntity<R> orgUpdate(final UUID orgId, final UUID id, final Object body,
                                       final Class<R> responseType) {

        return baseService.findByIdAndOrgId(id, orgId)
                .map(e -> patchComponent.patch(e, body))
                .map(baseService::update)
                .map(e -> modelMapper.map(e, responseType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
