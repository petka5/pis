package org.petka.pis.delegators;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.api.OperatorOrgsApiDelegate;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.model.OrgPageResponse;
import org.petka.pis.model.OrgRequest;
import org.petka.pis.model.OrgResponse;
import org.petka.pis.persistence.entities.Organization;
import org.petka.pis.services.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OperatorOrgsDelegator extends BaseDelegate<Organization, OrgResponse, OrgPageResponse>
        implements OperatorOrgsApiDelegate {


    public OperatorOrgsDelegator(final ModelMapper modelMapper,
                                 final OrganizationService organizationService,
                                 final SpecificationComponent specificationComponent,
                                 final PatchComponent patchComponent) {
        super(modelMapper, organizationService, specificationComponent, patchComponent);
    }

    @Override
    public ResponseEntity<OrgResponse> operatorAddOrg(final OrgRequest org) {
        return create(org, Organization.class, OrgResponse.class);
    }

    @Override
    public ResponseEntity<Void> operatorDeleteOrg(final UUID id) {
        return deleteById(id);
    }

    @Override
    public ResponseEntity<OrgResponse> operatorFindOrgById(final UUID id) {
        return findById(id, OrgResponse.class);
    }

    @Override
    public ResponseEntity<OrgPageResponse> operatorFindOrgs(final Integer page, final Integer size, final String sort,
                                                            final String filter,
                                                            final Boolean includeDeleted, final Boolean includeCount) {
        return findAll(page, size, sort, filter, includeDeleted, includeCount, OrgPageResponse.class);
    }

    @Override
    public ResponseEntity<OrgResponse> operatorUpdateOrg(final UUID id, final Object body) {
        return updateById(id, body, OrgResponse.class);
    }
}
