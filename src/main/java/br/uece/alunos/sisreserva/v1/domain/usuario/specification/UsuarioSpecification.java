package br.uece.alunos.sisreserva.v1.domain.usuario.specification;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class UsuarioSpecification {

    public static Specification<Usuario> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) return cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }
            if (filtros.containsKey("matricula")) {
                predicates.add(cb.equal(root.get("matricula"), filtros.get("matricula")));
            }
            if (filtros.containsKey("email")) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + ((String) filtros.get("email")).toLowerCase() + "%"));
            }
            if (filtros.containsKey("documentoFiscal")) {
                predicates.add(cb.like(cb.lower(root.get("documentoFiscal")), "%" + ((String) filtros.get("documentoFiscal")).toLowerCase() + "%"));
            }
            if (filtros.containsKey("instituicaoId")) {
                predicates.add(cb.equal(root.get("instituicao").get("id"), filtros.get("instituicaoId")));
            }

            if (filtros.containsKey("cargoId")) {
                var join = root.join("usuarioCargos");
                predicates.add(cb.equal(join.get("cargo").get("id"), filtros.get("cargoId")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
