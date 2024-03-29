package org.petka.pis.services;

import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;

import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.persistence.repositories.CustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.annotation.Validated;

@Validated
public class BaseService<T extends BaseEntity> {

    private final CustomRepository<T, UUID> repository;

    public BaseService(final CustomRepository<T, UUID> repository) {
        this.repository = repository;
    }

    /**
     * Performs search over the database by given specification.
     *
     * @param spec           Specification
     * @param page           pageable
     * @param includeDeleted include deleted
     * @param includeCount   returns either slice or page
     * @return result
     */
    public Slice<T> findAll(final Specification<T> spec, final Pageable page, final boolean includeDeleted,
                            final boolean includeCount) {
        return repository.findAll(spec, page, includeDeleted, includeCount);
    }

    /**
     * Creates new T in the database.
     *
     * @param entity new entity.
     * @return stored entity.
     */
    public T create(final @Valid T entity) {
        return repository.save(entity);
    }


    /**
     * Find entity by id.
     *
     * @param id id
     * @return entity
     */
    public Optional<T> findById(final UUID id) {
        return repository.findById(id);
    }


    /**
     * Update entity.
     *
     * @param entity entity to be saved.
     * @return entity
     */
    public T update(final @Valid T entity) {
        return repository.save(entity);
    }

    /**
     * Delete entity.
     *
     * @param entity entity to be deleted.
     * @return entity
     */
    public T delete(final T entity) {
        repository.deleteById(entity.getId());
        return entity;
    }
}
