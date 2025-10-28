package br.uece.alunos.sisreserva.v1.domain.espaco.specification;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import jakarta.persistence.criteria.JoinType;
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
     * Método que cria uma Specification baseada nos parâmetros passados.
     * A ideia é receber filtros e construir uma query dinâmica.
     * 
     * @param id identificador do espaço
     * @param departamentoId identificador do departamento
     * @param localizacaoId identificador da localização
     * @param tipoEspacoId identificador do tipo de espaço
     * @param tipoAtividadeId identificador do tipo de atividade (verifica se está na lista)
     * @param multiusuario flag de multiusuário
     * @return Specification<Espaco> que pode ser usada no repository para consultas dinâmicas
     */
    public static Specification<Espaco> byFilter(
            String id,
            String departamentoId,
            String localizacaoId,
            String tipoEspacoId,
            String tipoAtividadeId,
            Boolean multiusuario
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
            // Para filtrar por tipo de atividade, verifica se o ID está na lista de tipos de atividade do espaço
            if (tipoAtividadeId != null && !tipoAtividadeId.isBlank()) {
                var tiposAtividadeJoin = root.join("tiposAtividade", JoinType.LEFT);
                predicates.add(cb.equal(tiposAtividadeJoin.get("id"), tipoAtividadeId));
            }
            if (multiusuario != null) {
                predicates.add(cb.equal(root.get("multiusuario"), multiusuario));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
