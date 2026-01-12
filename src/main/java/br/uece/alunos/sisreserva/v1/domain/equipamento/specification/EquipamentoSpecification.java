package br.uece.alunos.sisreserva.v1.domain.equipamento.specification;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.StatusEquipamento;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipamentoSpecification {

    /**
     * Cria uma Specification para filtrar equipamentos baseado em múltiplos critérios.
     * Suporta filtro de multiusuário e restrição para usuários externos.
     * 
     * @param id identificador do equipamento
     * @param tombamento número de tombamento do equipamento
     * @param status status do equipamento
     * @param tipoEquipamento identificador do tipo de equipamento
     * @param multiusuario flag de multiusuário
     * @param restringirApenasMultiusuario flag para restringir apenas equipamentos multiusuário (para usuários externos)
     * @return Specification<Equipamento> que pode ser usada no repository para consultas dinâmicas
     */
    public static Specification<Equipamento> byFilters(
            String id,
            String tombamento,
            String status,
            String tipoEquipamento,
            Boolean multiusuario,
            Boolean restringirApenasMultiusuario
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null && !id.isBlank()) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (tombamento != null && !tombamento.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("tombamento")), "%" + tombamento.toLowerCase() + "%"));
            }

            if (status != null && !status.isBlank()) {
                try {
                    StatusEquipamento statusEnum = StatusEquipamento.valueOf(status.toUpperCase());
                    predicates.add(cb.equal(root.get("status"), statusEnum));
                } catch (IllegalArgumentException ignored) {
                    // Ignora status inválido
                }
            }

            if (tipoEquipamento != null && !tipoEquipamento.isBlank()) {
                predicates.add(cb.equal(root.get("tipoEquipamento").get("id"), tipoEquipamento));
            }

            if (multiusuario != null) {
                predicates.add(cb.equal(root.get("multiusuario"), multiusuario));
            }
            
            // Restrição para usuários externos: apenas equipamentos multiusuário
            if (restringirApenasMultiusuario != null && restringirApenasMultiusuario) {
                predicates.add(cb.isTrue(root.get("multiusuario")));
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
