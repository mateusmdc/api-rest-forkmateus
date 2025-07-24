package br.uece.alunos.sisreserva.v1.domain.projeto.specification;

import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que implementa Specifications para consulta dinâmica da entidade Projeto.
 * Pode ser usada para montar filtros complexos de forma reutilizável.
 */

public class ProjetoSpecification {
    /**
     * Metodo que cria uma Specification baseada nos parâmetros passados.
     * A ideia é receber um mapa de filtros (nome do campo -> valor)
     * e construir uma query dinâmica.
     * @return Specification<Projeto> que pode ser usada no repository para consultas dinâmicas
     */
    public static Specification<Projeto> byFilter(
        String id, 
        String nome, 
        String descricao, 
        LocalDate dataInicio, 
        LocalDate dataFim, 
        String usuarioResponsavelId, 
        String instituicaoId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null && !id.isBlank()) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.equal(root.get("nome"), nome));
            }
            if (descricao != null && !descricao.isBlank()) {
                predicates.add(cb.equal(root.get("descricao"), descricao));
            }
            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataInicio"), dataInicio));
            }
            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataFim"), dataFim));
            }
            if (usuarioResponsavelId != null && !usuarioResponsavelId.isBlank()) {
                predicates.add(cb.equal(root.get("usuarioResponsavel").get("id"), usuarioResponsavelId));
            }
            if (instituicaoId != null && !instituicaoId.isBlank()) {
                predicates.add(cb.equal(root.get("instituicao").get("id"), instituicaoId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
