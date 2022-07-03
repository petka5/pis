package org.petka.pis.services;

import org.petka.pis.persistence.entities.Organization;
import org.petka.pis.persistence.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService extends BaseService<Organization> {

    @Autowired
    public OrganizationService(final OrganizationRepository repository) {
        super(repository);
    }
}
