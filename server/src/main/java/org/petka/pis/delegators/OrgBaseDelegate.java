package org.petka.pis.delegators;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.petka.pis.components.PatchComponent;
import org.petka.pis.components.SpecificationComponent;
import org.petka.pis.persistence.entities.OrgBaseEntity;
import org.petka.pis.services.OrgBaseService;
import org.springframework.http.ResponseEntity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Model mapper could be changed.")
public class OrgBaseDelegate<E extends OrgBaseEntity, R, P> extends BaseDelegate<E, R, P> {

    private final OrgBaseService<E> orgBaseService;

    public OrgBaseDelegate(final ModelMapper modelMapper, final OrgBaseService<E> orgBaseService,
                           final SpecificationComponent specificationComponent,
                           final PatchComponent patchComponent) {
        super(modelMapper, orgBaseService, specificationComponent, patchComponent);
        this.orgBaseService = orgBaseService;
    }

    /**
     * Get entity by id and org id.
     *
     * @param orgId        org id
     * @param id           id
     * @param responseType response type
     * @return entity
     */
    public ResponseEntity<R> orgFindById(final UUID orgId, final UUID id, final Class<R> responseType) {
        return orgBaseService.findByIdAndOrgId(id, orgId)
                .map(e -> findById(e, responseType))
                .orElseGet(ResponseEntity.internalServerError()::build);
    }

    /**
     * Delete entity.
     *
     * @param orgId org id
     * @param id    id
     * @return no content
     */
    public ResponseEntity<Void> orgDeleteById(final UUID orgId, final UUID id) {
        return orgBaseService.findByIdAndOrgId(id, orgId)
                .map(this::delete)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    /**
     * Update entity.
     *
     * @param orgId        org id
     * @param id           id
     * @param body         patch request
     * @param responseType response type
     * @return updated entity
     */
    public ResponseEntity<R> orgUpdateById(final UUID orgId, final UUID id, final Object body,
                                           final Class<R> responseType) {
        return orgBaseService.findByIdAndOrgId(id, orgId)
                .map(e -> updateById(e, body, responseType))
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
