package org.petka.pis.services;

import java.util.Optional;
import java.util.UUID;

import org.petka.pis.persistence.entities.OrgBaseEntity;
import org.petka.pis.persistence.repositories.OrgCustomRepository;

public class OrgBaseService<T extends OrgBaseEntity> extends BaseService<T> {

    private final OrgCustomRepository<T, UUID> repository;

    public OrgBaseService(final OrgCustomRepository<T, UUID> repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Find entity by id and orgId.
     *
     * @param id    id
     * @param orgId orgId
     * @return entity
     */
    public Optional<T> findByIdAndOrgId(final UUID id, final UUID orgId) {
        return repository.findByIdAndOrgId(id, orgId);
    }
}
