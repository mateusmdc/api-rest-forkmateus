package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.specification;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamento;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class TipoEquipamentoSpecification {

    public static Specification<TipoEquipamento> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates = cb.and(predicates, cb.equal(root.get("id"), filtros.get("id")));
            }

            return predicates;
        };
    }
}
