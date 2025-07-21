package br.uece.alunos.sisreserva.v1.domain.equipamento.specification;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.StatusEquipamento;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipamentoSpecification {

    public static Specification<Equipamento> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) return cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }

            if (filtros.containsKey("tombamento")) {
                String tombamento = filtros.get("tombamento").toString().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("tombamento")), "%" + tombamento + "%"));
            }

            if (filtros.containsKey("status")) {
                try {
                    StatusEquipamento status = StatusEquipamento.valueOf(filtros.get("status").toString());
                    predicates.add(cb.equal(root.get("status"), status));
                } catch (IllegalArgumentException ignored) {
                    // Ignora status inválido
                }
            }

            if (filtros.containsKey("tipoEquipamento")) {
                predicates.add(cb.equal(root.get("tipoEquipamento").get("id"), filtros.get("tipoEquipamento")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Equipamento> comId(String id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Equipamento> comTombamento(String tombamento) {
        return (root, query, cb) -> tombamento == null ? null :
                cb.like(cb.lower(root.get("tombamento")), "%" + tombamento.toLowerCase() + "%");
    }

    public static Specification<Equipamento> comStatus(String status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            try {
                StatusEquipamento statusEnum = StatusEquipamento.valueOf(status.toUpperCase());
                return cb.equal(root.get("status"), statusEnum);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    public static Specification<Equipamento> comTipoEquipamento(String tipoId) {
        return (root, query, cb) -> tipoId == null ? null :
                cb.equal(root.get("tipoEquipamento").get("id"), tipoId);
    }
}
