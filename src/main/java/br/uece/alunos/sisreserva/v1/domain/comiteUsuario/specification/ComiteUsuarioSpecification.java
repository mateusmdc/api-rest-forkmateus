package br.uece.alunos.sisreserva.v1.domain.comiteUsuario.specification;

import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.ComiteUsuario;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComiteUsuarioSpecification {

    public static Specification<ComiteUsuario> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) {
                return cb.conjunction();
            }

            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }

            if (filtros.containsKey("comiteId")) {
                predicates.add(cb.equal(root.get("comite").get("id"), filtros.get("comiteId")));
            }

            if (filtros.containsKey("usuarioId")) {
                predicates.add(cb.equal(root.get("usuario").get("id"), filtros.get("usuarioId")));
            }

            if (filtros.containsKey("departamentoId")) {
                predicates.add(cb.equal(root.get("departamento").get("id"), filtros.get("departamentoId")));
            }

            if (filtros.containsKey("portaria")) {
                predicates.add(cb.like(cb.lower(root.get("portaria")), "%" + filtros.get("portaria").toString().toLowerCase() + "%"));
            }

            if (filtros.containsKey("isTitular")) {
                predicates.add(cb.equal(root.get("isTitular"), filtros.get("isTitular")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
