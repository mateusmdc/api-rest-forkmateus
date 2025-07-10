package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.specification;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspaco;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorEspacoSpecification {

    public static Specification<GestorEspaco> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) return cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }

            if (filtros.containsKey("espacoId")) {
                predicates.add(cb.equal(root.get("espaco").get("id"), filtros.get("espacoId")));
            }

            if (filtros.containsKey("gestorId")) {
                predicates.add(cb.equal(root.get("gestor").get("id"), filtros.get("gestorId")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
