package br.uece.alunos.sisreserva.v1.domain.espaco.specification;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que implementa Specifications para consulta dinâmica da entidade Espaco.
 * Pode ser usada para montar filtros complexos de forma reutilizável.
 */
public class EspacoSpecification {

    /**
     * Metodo que cria uma Specification baseada nos parâmetros passados.
     * A ideia é receber um mapa de filtros (nome do campo -> valor)
     * e construir uma query dinâmica.
     * @return Specification<Espaco> que pode ser usada no repository para consultas dinâmicas
     */
    public static Specification<Espaco> byFilter(
            String id,
            String departamentoId,
            String localizacaoId,
            String tipoEspacoId,
            String tipoAtividadeId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null && !id.isBlank()) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (departamentoId != null && !departamentoId.isBlank()) {
                predicates.add(cb.equal(root.get("departamento").get("id"), departamentoId));
            }
            if (localizacaoId != null && !localizacaoId.isBlank()) {
                predicates.add(cb.equal(root.get("localizacao").get("id"), localizacaoId));
            }
            if (tipoEspacoId != null && !tipoEspacoId.isBlank()) {
                predicates.add(cb.equal(root.get("tipoEspaco").get("id"), tipoEspacoId));
            }
            if (tipoAtividadeId != null && !tipoAtividadeId.isBlank()) {
                predicates.add(cb.equal(root.get("tipoAtividade").get("id"), tipoAtividadeId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
