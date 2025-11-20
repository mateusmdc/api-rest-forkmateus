package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.specification;

import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspaco;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe para construir especificações de consultas dinâmicas para SecretariaEspaco.
 * Permite filtrar secretarias de espaço por diversos critérios.
 */
public class SecretariaEspacoSpecification {

    /**
     * Constrói uma especificação baseada nos filtros fornecidos
     * 
     * @param filtros Mapa contendo os filtros a serem aplicados.
     *                Filtros disponíveis: id, espacoId, secretariaId, todos
     * @return Specification para ser usada em consultas JPA
     */
    public static Specification<SecretariaEspaco> byFilters(Map<String, Object> filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros == null) return cb.conjunction();

            // Filtro por ID da secretaria de espaço
            if (filtros.containsKey("id")) {
                predicates.add(cb.equal(root.get("id"), filtros.get("id")));
            }

            // Filtro por ID do espaço
            if (filtros.containsKey("espacoId")) {
                predicates.add(cb.equal(root.get("espaco").get("id"), filtros.get("espacoId")));
            }

            // Filtro por ID do usuário da secretaria
            if (filtros.containsKey("secretariaId")) {
                predicates.add(cb.equal(root.get("usuarioSecretaria").get("id"), filtros.get("secretariaId")));
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
