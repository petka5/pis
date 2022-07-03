package org.petka.pis.persistence.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.petka.pis.persistence.entities.OrgBaseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

@NoRepositoryBean
public interface OrgCustomRepository<T extends OrgBaseEntity, K extends Serializable> extends CustomRepository<T, K> {

    /**
     * Selecting entity by id and orgId and takes into account <B>deleted</B> field.
     *
     * @param id    primary key
     * @param orgId orgId
     * @return entity
     */
    @NonNull
    @Query("select e from #{#entityName} e where e.id = ?1 and e.orgId= ?2 and e.deleted = false")
    Optional<T> findByIdAndOrgId(@NonNull K id, @NonNull K orgId);
}
