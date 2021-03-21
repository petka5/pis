package org.petka.pis.persistence.restquery;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class RestQuery {

    static final int DEFAULT_PAGE_SIZE = 50;
    static final int MAX_PAGE_SIZE = 1000;

    private Collection<SearchCriteria> criteria = new LinkedList<>();

    private List<Sort.Order> orders = new LinkedList<>();

    private Pageable pagination;

    public RestQuery() {

    }

    public RestQuery(final Collection<SearchCriteria> criteria, final List<Sort.Order> orders, final Pageable pagination) {
        this.criteria = criteria;
        this.orders = orders;
        this.pagination = pagination;
    }

    /**
     * Add search criteria to the query.
     *
     * @param criterion search criteria.
     * @return this
     */
    public RestQuery add(final SearchCriteria criterion) {
        criteria.add(criterion);
        return this;
    }

    /**
     * Get the list of {@link SearchCriteria}.
     *
     * @return Collection of SearchCriteria
     */
    public Collection<SearchCriteria> getCriteria() {
        return criteria;
    }

    /**
     * Adding Order to the query.
     *
     * @param order order
     * @return this
     */
    public RestQuery add(final Sort.Order order) {
        orders.add(order);
        return this;
    }

    /**
     * Adding pagination to the query.
     *
     * @param page number of the requested page
     * @param size size of the page
     * @return this
     */
    public RestQuery paginate(final int page, final int size) {
        pagination = PageRequest.of(page, size);
        return this;
    }

    /**
     * Get query pagination.
     *
     * @return Pageabl
     */
    public Pageable getPagination() {
        if (Objects.isNull(pagination)) {
            return PageRequest.of(0, DEFAULT_PAGE_SIZE);
        }
        if (pagination.getPageSize() > MAX_PAGE_SIZE) {
            return PageRequest.of(pagination.getPageNumber(), MAX_PAGE_SIZE, pagination.getSort());
        }
        return pagination;
    }
}
