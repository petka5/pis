package org.petka.pis.components;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.turkraft.springfilter.boot.FilterSpecification;

/**
 * SpecificationComponent.
 */
@Component
public class SpecificationComponent {

    /**
     * Creates specification from the filter param.
     *
     * @param <T>    entity type
     * @param filter filter request parameter
     * @return Specification
     */
    public <T> Specification<T> createSpecification(final String filter) {

        return Optional.ofNullable(filter).map(FilterSpecification<T>::new).map(Specification.class::cast)
                .orElse(Specification.where(null));
    }

    /**
     * Creates organization specification from the filter param.
     *
     * @param filter request filter
     * @param orgId  org id
     * @param <T>    entity type
     * @return Specification
     */
    public <T> Specification<T> createSpecification(final String filter, final UUID orgId) {
        return new FilterSpecification<>(
                Optional.ofNullable(filter).map("orgId:'" .concat(orgId.toString()).concat("' and ")::concat)
                        .orElse("orgId:'" .concat(orgId.toString()).concat("'")));
    }


    /**
     * Creates pageable from the request parameters.
     *
     * @param page page number
     * @param size page size
     * @param sort sort
     * @return Pageable
     */
    public Pageable createPageRequest(final Integer page, final Integer size, final String sort) {
        if (Objects.isNull(sort)) {
            return PageRequest.of(page, size);
        }
        String[] sortSplit = sort.split(":");
        Sort pageSort;
        if (sortSplit.length > 1) {
            pageSort = Sort.by(Sort.Direction.fromString(sortSplit[0]), sortSplit[1].split(","));
        } else {
            pageSort = Sort.by(sortSplit[0].split(","));
        }
        return PageRequest.of(page, size, pageSort);
    }
}
