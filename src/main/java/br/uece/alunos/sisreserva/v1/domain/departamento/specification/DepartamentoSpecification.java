package br.uece.alunos.sisreserva.v1.domain.departamento.specification;

import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class DepartamentoSpecification {

    public static Specification<Departamento> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates.getExpressions().add(cb.equal(root.get("id"), filtros.get("id")));
            }

            return predicates;
        };
    }
}
