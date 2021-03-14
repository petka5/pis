package org.petka.pis.persistence.restquery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

public class CustomSpecification<T> implements Specification<T> {

    static final long serialVersionUID = 42L;

    private final transient SearchCriteria criteria;

    public CustomSpecification(final SearchCriteria criteria) {
        this.criteria = criteria;
    }


    @Override
    public Predicate toPredicate(final @NonNull Root<T> root, final @NonNull CriteriaQuery<?> query,
                                 final @NonNull CriteriaBuilder criteriaBuilder) {

        switch (criteria.getOperation()) {
            case EQUALITY:
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(root.get(
                        criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return criteriaBuilder.lessThan(root.get(
                        criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return criteriaBuilder.like(root.get(
                        criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return criteriaBuilder.like(root.get(
                        criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }
}
