package org.petka.pis.persistence.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * BaseRepository interface.
 *
 * @param <T> type of the entity.
 * @param <K> type of the primary key.
 */
@NoRepositoryBean
public interface CustomRepository<T, K extends Serializable> extends JpaRepository<T, K> {


    /**
     * Method executes Specification request over the database.
     *
     * @param spec           Specification
     * @param pageable       pageable
     * @param includeDeleted whether deleted records should be included or not in the result set.
     * @param includeCount   returns either slice or page
     * @return Page of entities
     */
    Slice<T> findAll(@Nullable Specification<T> spec, Pageable pageable, boolean includeDeleted, boolean includeCount);

    /**
     * Overriding method to takes into account <B>deleted</B> field.
     *
     * @param k primary key
     * @return entity
     */
    @Override
    @NonNull
    @Query("select e from #{#entityName} e where e.id = ?1 and e.deleted = false")
    Optional<T> findById(@NonNull K k);
}
