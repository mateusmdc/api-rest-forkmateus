package br.uece.alunos.sisreserva.v1.domain.instituicao.specification;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InstituicaoSpecification {

    public static Specification<Instituicao> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) return cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
