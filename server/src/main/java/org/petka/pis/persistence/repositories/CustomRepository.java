package org.petka.pis.persistence.repositories;

import java.io.Serializable;

import org.petka.pis.persistence.restquery.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * BaseRepository interface.
 *
 * @param <T> type of the entity.
 * @param <K> type of the primary key.
 */
@NoRepositoryBean
public interface CustomRepository<T, K extends Serializable> extends JpaRepository<T, K> {

    /**
     * Method that executes query on the database.
     *
     * @param query Query @{@link Query}.
     * @return Paged result
     */
    Page<T> search(Query query);
}
