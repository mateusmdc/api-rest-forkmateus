package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.specification;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspaco;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipamentoEspacoSpecification {

    public static Specification<EquipamentoEspaco> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) return cb.conjunction();

            // só registros com dataRemocao == null
            predicates.add(cb.isNull(root.get("dataRemocao")));

            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }

            if (filtros.containsKey("equipamentoId")) {
                predicates.add(cb.equal(root.get("equipamento").get("id"), filtros.get("equipamentoId")));
            }

            if (filtros.containsKey("tipoEquipamentoId")) {
                predicates.add(cb.equal(root.get("equipamento").get("tipoEquipamento").get("id"), filtros.get("tipoEquipamentoId")));
            }

            if (filtros.containsKey("espacoId")) {
                predicates.add(cb.equal(root.get("espaco").get("id"), filtros.get("espacoId")));
            }

            if (filtros.containsKey("dataInicio")) {
                LocalDateTime dataInicio = (LocalDateTime) filtros.get("dataInicio");
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataAlocacao"), dataInicio));
            }

            if (filtros.containsKey("dataFim")) {
                LocalDateTime dataFim = (LocalDateTime) filtros.get("dataFim");
                predicates.add(cb.lessThanOrEqualTo(root.get("dataAlocacao"), dataFim));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
