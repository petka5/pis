package org.petka.pis.persistence.repositories;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.petka.pis.persistence.restquery.CustomSpecification;
import org.petka.pis.persistence.restquery.Query;
import org.petka.pis.persistence.restquery.SearchCriteria;
import org.springframework.data.domain.Page;
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

    /**
     * Method that executes query on the database.
     *
     * @param query Query @{@link Query}.
     * @return Paged result
     */
    @Override
    public Page<T> search(final Query query) {
        return findAll(convertQueryToSpecification(query), query.getPagination());
    }

    private CustomSpecification<T> convertQueryToSpecification(final Query query) {
        CustomSpecification<T> specification = null;

        for (SearchCriteria criteria : query.getCriteria()) {
            specification = addCriteria(specification, criteria);
        }

        return specification;
    }

    private CustomSpecification<T> addCriteria(final CustomSpecification<T> specification,
                                               final SearchCriteria criteria) {
        CustomSpecification<T> localSpecification = new CustomSpecification<>(criteria);

        if (Objects.isNull(specification)) {
            return localSpecification;
        }
        return (CustomSpecification<T>) specification.and(localSpecification);
    }
}
