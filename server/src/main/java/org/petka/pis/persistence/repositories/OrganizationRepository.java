package org.petka.pis.persistence.repositories;

import java.util.UUID;

import org.petka.pis.persistence.entities.Organization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CustomRepository<Organization, UUID> {

}
