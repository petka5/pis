package org.petka.pis.persistence.repositories;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * Implementation of the BaseRepository.
 *
 * @param <T> type of the entity.
 * @param <K> type of the primary key.
 */
public class CustomRepositoryImpl<T, K extends Serializable> extends SimpleJpaRepository<T, K>
        implements CustomRepository<T, K> {


    public CustomRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Page<T> findAll(final Specification<T> spec, final Pageable pageable, final boolean includeDeleted) {

        Specification<T> querySpec = spec;
        if (!includeDeleted) {
            querySpec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false));
        }
        return findAll(querySpec, pageable);
    }
}
