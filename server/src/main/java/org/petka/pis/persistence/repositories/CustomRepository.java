package org.petka.pis.persistence.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.petka.pis.persistence.restquery.RestQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

/**
 * BaseRepository interface.
 *
 * @param <T> type of the entity.
 * @param <K> type of the primary key.
 */
@NoRepositoryBean
public interface CustomRepository<T, K extends Serializable> extends JpaRepository<T, K> {

    /**
     * Method that executes query on the database and returns non deleted records.
     *
     * @param restQuery Query @{@link RestQuery}.
     * @return Paged result
     */
    Page<T> search(RestQuery restQuery);

    /**
     * Method that executes query on the database, based on includeDeleted it could returns deleted records as well.
     *
     * @param restQuery      Query @{@link RestQuery}.
     * @param includeDeleted denotes whether deleted records should be included in the result set.
     * @return Paged result
     */
    Page<T> search(RestQuery restQuery, boolean includeDeleted);

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
