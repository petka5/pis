package org.petka.pis.persistence.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.NonNull;

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
    public Slice<T> findAll(final Specification<T> spec, final Pageable pageable, final boolean includeDeleted,
                            final boolean includeCount) {

        Specification<T> querySpec = spec;
        if (!includeDeleted) {
            querySpec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false));
        }

        if (includeCount) {
            return findAll(querySpec, pageable);
        }
        return findAllSlice(querySpec, pageable);
    }

    private Slice<T> findAllSlice(final Specification<T> spec, final Pageable pageable) {
        TypedQuery<T> query = getQuery(spec, pageable);
        query.setMaxResults(pageable.getPageSize() + 1);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        List<T> resultList = query.getResultList();
        boolean hasNext = pageable.isPaged() && resultList.size() == pageable.getPageSize() + 1;

        return new SliceImpl<>(hasNext ? resultList.subList(0, pageable.getPageSize()) : resultList, pageable,
                               hasNext);
    }

    @NonNull
    @Override
    public Optional<T> findByIdAndOrgId(@SuppressWarnings("unused") @NonNull final K id,
                                        @SuppressWarnings("unused") @NonNull final K orgId) {
        return Optional.empty();
    }
}
