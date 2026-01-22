package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.specification;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacos;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Specification para aplicar filtros dinâmicos em consultas de GestorComplexoEspacos.
 */
public class GestorComplexoEspacosSpecification {

    /**
     * Cria uma Specification com base nos filtros fornecidos.
     * 
     * @param filtros Mapa contendo os filtros (id, complexoEspacosId, gestorId, todos)
     * @return Specification com os predicados aplicados
     */
    public static Specification<GestorComplexoEspacos> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) return cb.conjunction();

            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }

            if (filtros.containsKey("complexoEspacosId")) {
                predicates.add(cb.equal(root.get("complexoEspacos").get("id"), filtros.get("complexoEspacosId")));
            }

            if (filtros.containsKey("gestorId")) {
                predicates.add(cb.equal(root.get("usuarioGestor").get("id"), filtros.get("gestorId")));
            }

            // Só aplica o filtro estaAtivo=true se 'todos' não for true
            boolean incluirTodos = filtros.containsKey("todos") && Boolean.TRUE.equals(filtros.get("todos"));
            if (!incluirTodos) {
                predicates.add(cb.isTrue(root.get("estaAtivo")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
