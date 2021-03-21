package org.petka.pis.persistence.repositories;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.petka.pis.persistence.restquery.CustomSpecification;
import org.petka.pis.persistence.restquery.RestQuery;
import org.petka.pis.persistence.restquery.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the BaseRepository.
 *
 * @param <T> type of the entity.
 * @param <K> type of the primary key.
 */
@Slf4j
public class CustomRepositoryImpl<T, K extends Serializable> extends SimpleJpaRepository<T, K>
        implements CustomRepository<T, K> {

    private final EntityManager entityManager;

    public CustomRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }


    /**
     * Method that executes query on the database and returns non deleted records.
     *
     * @param restQuery Query @{@link RestQuery}.
     * @return Paged result
     */
    @Override
    public Page<T> search(RestQuery restQuery) {
        return search(restQuery, false);
    }

    /**
     * Method that executes query on the database, based on includeDeleted it could returns deleted records as well.
     *
     * @param restQuery      Query @{@link RestQuery}.
     * @param includeDeleted denotes whether deleted records should be included in the result set.
     * @return Paged result
     */
    @Override
    public Page<T> search(final RestQuery restQuery, boolean includeDeleted) {
        Session session = null;
        try {
            session = entityManager.unwrap(Session.class);
            if (includeDeleted) {
                session.disableFilter(IS_DELETED_FILTER_NAME);
            } else {
                session.enableFilter(IS_DELETED_FILTER_NAME);
            }

            return findAll(convertQueryToSpecification(restQuery), restQuery.getPagination());
        } catch (Exception e) {
            log.error("Error execution db query {}.", e.getMessage());
        } finally {
            if (Objects.nonNull(session) && includeDeleted) {
                session.enableFilter(IS_DELETED_FILTER_NAME);
            }
        }
        return null;
    }

    private CustomSpecification<T> convertQueryToSpecification(final RestQuery restQuery) {
        CustomSpecification<T> specification = null;

        for (SearchCriteria criteria : restQuery.getCriteria()) {
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
