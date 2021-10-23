package org.petka.pis.services;

import java.util.Optional;
import java.util.UUID;

import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.persistence.repositories.CustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
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
    public T create(final T entity) {
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
     * Delete entity by id.
     *
     * @param id id
     * @return entity
     */
    public Optional<T> deleteById(final UUID id) {
        return this.findById(id).map(this::deleteById);
    }

    /**
     * Update entity.
     *
     * @param entity
     * @return entity
     */
    public T update(final T entity) {
        return repository.save(entity);
    }

    private T deleteById(final T entity) {
        repository.deleteById(entity.getId());
        return entity;
    }
}
