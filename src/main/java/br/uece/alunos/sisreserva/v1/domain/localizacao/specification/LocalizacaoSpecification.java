package br.uece.alunos.sisreserva.v1.domain.localizacao.specification;

import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class LocalizacaoSpecification {

    public static Specification<Localizacao> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates = cb.and(predicates, cb.equal(root.get("id"), filtros.get("id")));
            }

            return predicates;
        };
    }
}
