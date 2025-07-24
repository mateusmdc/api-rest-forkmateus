package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.specification;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que implementa Specifications para consulta dinâmica da entidade SolicitacaoReserva.
 * Pode ser usada para montar filtros complexos de forma reutilizável.
 */
public class SolicitacaoReservaSpecification {

    /**
     * Cria uma Specification baseada nos parâmetros passados.
     * A ideia é receber um mapa de filtros (nome do campo -> valor)
     * e construir uma query dinâmica.
     * Permite filtrar por id, datas, espaço, usuário solicitante, status e projeto.
     * @return Specification<Projeto> que pode ser usada no repository para consultas dinâmicas
     */
    public static Specification<SolicitacaoReserva> byFilter(
        String id,
        LocalDate dataInicio,
        LocalDate dataFim,
        String espacoId,
        String usuarioSolicitanteId,
        Integer statusCodigo,
        String projetoId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null && !id.isBlank()) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataInicio"), dataInicio));
            }
            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataFim"), dataFim));
            }
            if (espacoId != null && !espacoId.isBlank()) {
                predicates.add(cb.equal(root.get("espaco").get("id"), espacoId));
            }
            if (usuarioSolicitanteId != null && !usuarioSolicitanteId.isBlank()) {
                predicates.add(cb.equal(root.get("usuarioSolicitante").get("id"), usuarioSolicitanteId));
            }
            if (statusCodigo != null) {
                predicates.add(cb.equal(root.get("status"), statusCodigo));
            }
            if (projetoId != null && !projetoId.isBlank()) {
                predicates.add(cb.equal(root.get("projeto").get("id"), projetoId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
