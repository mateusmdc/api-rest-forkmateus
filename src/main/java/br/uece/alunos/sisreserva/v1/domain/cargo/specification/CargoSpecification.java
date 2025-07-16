package br.uece.alunos.sisreserva.v1.domain.cargo.specification;

import br.uece.alunos.sisreserva.v1.domain.cargo.Cargo;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class CargoSpecification {

    public static Specification<Cargo> byFilters(Map<String, Object> filters) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (filters.containsKey("id")) {
                predicate = cb.and(predicate, cb.equal(root.get("id"), filters.get("id")));
            }

            return predicate;
        };
    }
}
