package br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.specification;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenerico;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por criar Specifications para filtros dinâmicos de equipamentos genéricos.
 * Utiliza o padrão Specification do Spring Data JPA para construir queries dinâmicas.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public class EquipamentoGenericoSpecification {

    /**
     * Cria uma Specification para filtrar equipamentos genéricos baseado em múltiplos critérios.
     * 
     * @param id identificador do equipamento genérico
     * @param nome nome do equipamento genérico (busca parcial case-insensitive)
     * @return Specification que pode ser usada no repository para consultas dinâmicas
     */
    public static Specification<EquipamentoGenerico> byFilters(String id, String nome) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por ID (busca exata)
            if (id != null && !id.isBlank()) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            // Filtro por nome (busca parcial case-insensitive)
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
