package org.petka.pis.persistence.repositories;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.petka.pis.persistence.restquery.CustomSpecification;
import org.petka.pis.persistence.restquery.RestQuery;
import org.petka.pis.persistence.restquery.SearchCriteria;
import org.petka.pis.persistence.restquery.SearchOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
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


    public CustomRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
    }


    /**
     * Method that executes query on the database and returns non deleted records.
     *
     * @param restQuery Query @{@link RestQuery}.
     * @return Paged result
     */
    @Override
    public Page<T> search(final RestQuery restQuery) {
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
    public Page<T> search(final RestQuery restQuery, final boolean includeDeleted) {

        if (!includeDeleted) {
            restQuery.add(SearchCriteria.builder().key("deleted").operation(SearchOperation.EQUALITY).value(false)
                                  .build());
        }

        return findAll(convertQueryToSpecification(restQuery), restQuery.getPagination());
    }

    private Specification<T> convertQueryToSpecification(final RestQuery restQuery) {

        Specification<T> result = null;
        for (SearchCriteria criteria : restQuery.getCriteria()) {
            if (Objects.isNull(result)) {
                result = new CustomSpecification<>(criteria);
            } else {
                result = Specification.where(result).and(new CustomSpecification<>(criteria));
            }
        }
        return result;
    }
}
