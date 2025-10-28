package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.specification;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacos;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification para consultas dinâmicas de ComplexoEspacos.
 */
public class ComplexoEspacosSpecification {

    public static Specification<ComplexoEspacos> byFilter(String id, String nome) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null && !id.isBlank()) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
