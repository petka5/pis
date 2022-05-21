package org.petka.pis.delegators;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.services.BaseService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * BaseDelegate performs base CRUD operation over specific entity.
 *
 * @param <E> entity
 * @param <R> response type
 * @param <P> page response type
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Model mapper could be changed.")
public class BaseDelegate<E extends BaseEntity, R, P> {

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
    public ResponseEntity<R> create(final Object request, final Class<E> entityType, final Class<R> responseType) {

        return Optional.of(modelMapper.map(request, entityType))
                .map(e -> create(e, responseType))
                .orElseGet(ResponseEntity.internalServerError()::build);

    }

    /**
     * Creates entity.
     *
     * @param orgId        org id
     * @param request      request
     * @param entityType   entity type
     * @param responseType response type
     * @return created entity
     */
    public ResponseEntity<R> orgCreate(final UUID orgId, final Object request, final Class<E> entityType,
                                       final Class<R> responseType) {

        return Optional.of(modelMapper.map(request, entityType))
                .map(e -> e.toBuilder().orgId(orgId).build())
                .map(entityType::cast)
                .map(e -> create(e, responseType))
                .orElseGet(ResponseEntity.internalServerError()::build);
    }

    private ResponseEntity<R> create(final E entity, final Class<R> responseType) {
        log.info("Creating {} entity", responseType.getSimpleName());
        return Optional.ofNullable(entity)
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
    public ResponseEntity<P> findAll(final Integer page, final Integer size, final String sort, final String filter,
                                     final @DefaultValue("false") Boolean includeDeleted,
                                     final @DefaultValue("false") Boolean includeCount,
                                     final Class<P> responseType) {

        return findAll(page, size, sort, specificationComponent.createSpecification(filter), includeDeleted,
                       includeCount, responseType);
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
    public ResponseEntity<P> orgFindAll(final UUID orgId, final Integer page, final Integer size,
                                        final String sort,
                                        final String filter, final Boolean includeDeleted,
                                        final Boolean includeCount,
                                        final Class<P> responseType) {

        return findAll(page, size, sort, specificationComponent.createSpecification(filter, orgId), includeDeleted,
                       includeCount, responseType);
    }


    private ResponseEntity<P> findAll(final Integer page, final Integer size,
                                      final String sort,
                                      final Specification<E> specification, final Boolean includeDeleted,
                                      final Boolean includeCount,
                                      final Class<P> responseType) {
        log.info("Getting all {} entity", responseType.getSimpleName());
        return Optional.of(baseService.findAll(specification,
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
                .map(e -> findById(e, responseType))
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
                .map(e -> findById(e, responseType))
                .orElseGet(ResponseEntity.internalServerError()::build);
    }


    private ResponseEntity<R> findById(final E entity, final Class<R> responseType) {
        log.info("Finding {} by id {}", entity.getClass().getSimpleName(), entity.getId());
        return Optional.of(entity)
                .map(e -> modelMapper.map(e, responseType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }


    /**
     * Delete entity by id.
     *
     * @param id id
     * @return no content
     */
    public ResponseEntity<Void> deleteById(final UUID id) {
        return baseService.findById(id)
                .map(this::delete)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    /**
     * Delete entity.
     *
     * @param orgId org id
     * @param id    id
     * @return no content
     */
    public ResponseEntity<Void> orgDeleteById(final UUID orgId, final UUID id) {
        return baseService.findByIdAndOrgId(id, orgId)
                .map(this::delete)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    private ResponseEntity<Void> delete(final E entity) {
        log.info("Deleting {} with id {}", entity.getClass().getSimpleName(), entity.getId());
        return Optional.of(entity)
                .map(baseService::delete)
                .map(e -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                .orElseGet(ResponseEntity.internalServerError()::build);
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
                .map(e -> updateById(e, body, responseType))
                .orElseGet(ResponseEntity.notFound()::build);

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
    public ResponseEntity<R> orgUpdateById(final UUID orgId, final UUID id, final Object body,
                                           final Class<R> responseType) {
        return baseService.findByIdAndOrgId(id, orgId)
                .map(e -> updateById(e, body, responseType))
                .orElseGet(ResponseEntity.notFound()::build);
    }

    private ResponseEntity<R> updateById(final E entity, final Object body,
                                         final Class<R> responseType) {
        log.info("Updating {} with id {}", entity.getClass().getSimpleName(), entity.getId());
        return Optional.of(entity)
                .map(e -> patchComponent.patch(e, body))
                .map(baseService::update)
                .map(e -> modelMapper.map(e, responseType))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.internalServerError()::build);
    }
}
