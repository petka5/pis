package org.petka.pis.components;

import java.util.Objects;

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
     * @param filter filter request parameter
     * @param <T>    entity type
     * @return Specification
     */
    public <T> Specification<T> createSpecification(final String filter) {

        if (Objects.nonNull(filter)) {
            return new FilterSpecification<>(filter);
        }

        return Specification.where(null);
    }

    /**
     * Creates pageble from the request parameters.
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
