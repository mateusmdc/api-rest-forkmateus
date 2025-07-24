package br.uece.alunos.sisreserva.v1.domain.comite.specification;

import br.uece.alunos.sisreserva.v1.domain.comite.Comite;
import br.uece.alunos.sisreserva.v1.domain.comite.TipoComite;
import br.uece.alunos.sisreserva.v1.domain.equipamento.StatusEquipamento;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComiteSpecification {

    public static Specification<Comite> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) return cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }

            if (filtros.containsKey("tipo")) {
                TipoComite tipoComite = TipoComite.valueOf(filtros.get("tipo").toString());
                predicates.add(cb.equal(root.get("tipo"), tipoComite));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
