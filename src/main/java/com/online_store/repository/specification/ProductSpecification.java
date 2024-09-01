package com.online_store.repository.specification;

import com.online_store.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    /**
     * Creates a JPA specification for products based on the given search criteria.
     * <p>
     * If a given parameter is null, it is ignored in the query.
     * </p>
     *
     * @param city             the city to search for
     * @param date             the earliest date to search for
     * @param participantCount the minimum participant count to search for
     * @return a JPA specification for products matching the search criteria
     */
    public static Specification<Product> searchProducts(String city, LocalDateTime date, Integer participantCount) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (city != null && !city.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("city"), city));
            }

            if (date != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), date));
            }

            if (participantCount != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("participantCount"), participantCount));
            }

            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private ProductSpecification() {
    }
}
