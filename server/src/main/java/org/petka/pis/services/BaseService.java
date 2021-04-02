package org.petka.pis.services;

import java.util.Optional;
import java.util.UUID;

import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.persistence.repositories.CustomRepository;
import org.petka.pis.persistence.restquery.RestQuery;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BaseService<T extends BaseEntity> {

    private final CustomRepository<T, UUID> repository;

    public BaseService(final CustomRepository<T, UUID> repository) {
        this.repository = repository;
    }

    /**
     * Search database by rest query.
     *
     * @param restQuery RestQuery
     * @return Page of T
     */
    public Page<T> search(final RestQuery restQuery) {
        return repository.search(restQuery, false);
    }

    /**
     * Search database by rest query.
     *
     * @param restQuery      RestQuery
     * @param includeDeleted should deleted record will be included in the result
     * @return Page of T
     */
    public Page<T> search(final RestQuery restQuery, final boolean includeDeleted) {
        return repository.search(restQuery, includeDeleted);
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

    private T deleteById(final T entity) {
        repository.deleteById(entity.getId());
        return entity;
    }
}
