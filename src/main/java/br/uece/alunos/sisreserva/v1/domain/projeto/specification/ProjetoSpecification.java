package br.uece.alunos.sisreserva.v1.domain.projeto.specification;

import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que implementa Specifications para consulta dinâmica da entidade Projeto.
 * Implementa filtros de permissão baseados no cargo do usuário autenticado.
 * <p>
 * Regras de visualização:
 * <ul>
 *   <li>Admin: vê todos os projetos</li>
 *   <li>Gestor: vê seus projetos + projetos de reservas dos espaços/equipamentos que gerencia</li>
 *   <li>Secretaria: vê seus projetos + projetos de reservas dos espaços/equipamentos da secretaria</li>
 *   <li>Usuário interno/externo: vê apenas seus projetos</li>
 * </ul>
 */
public class ProjetoSpecification {
    /**
     * Cria uma Specification baseada nos parâmetros passados.
     * Aplica filtros de dados E filtros de permissão baseados no cargo do usuário.
     * 
     * @param id Filtro por ID do projeto
     * @param nome Filtro por nome
     * @param descricao Filtro por descrição
     * @param dataInicio Filtro por data de início
     * @param dataFim Filtro por data de fim
     * @param usuarioResponsavelId Filtro por ID do usuário responsável
     * @param instituicaoId Filtro por ID da instituição
     * @param isAdmin Se o usuário é administrador (vê todos)
     * @param usuarioAutenticadoId ID do usuário autenticado (para filtrar seus projetos)
     * @param projetosVinculadosIds IDs dos projetos vinculados a reservas de espaços/equipamentos gerenciados
     * @return Specification com os filtros aplicados
     */
    public static Specification<Projeto> byFilter(
        String id, 
        String nome, 
        String descricao, 
        LocalDate dataInicio, 
        LocalDate dataFim, 
        String usuarioResponsavelId, 
        String instituicaoId,
        boolean isAdmin,
        String usuarioAutenticadoId,
        List<String> projetosVinculadosIds
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

            // Aplicar filtro de permissões baseado no cargo do usuário
            if (!isAdmin) {
                // Usuário não é admin: aplicar restrições de visualização
                List<Predicate> permissaoPredicates = new ArrayList<>();
                
                // 1. Pode ver seus próprios projetos (onde é responsável)
                if (usuarioAutenticadoId != null) {
                    permissaoPredicates.add(
                        cb.equal(root.get("usuarioResponsavel").get("id"), usuarioAutenticadoId)
                    );
                }
                
                // 2. Se for gestor ou secretaria, pode ver projetos vinculados a reservas que gerencia
                if (projetosVinculadosIds != null && !projetosVinculadosIds.isEmpty()) {
                    permissaoPredicates.add(
                        root.get("id").in(projetosVinculadosIds)
                    );
                }
                
                // Aplica OR: (meus projetos) OU (projetos vinculados a reservas que gerencio)
                if (!permissaoPredicates.isEmpty()) {
                    predicates.add(cb.or(permissaoPredicates.toArray(new Predicate[0])));
                } else if (usuarioAutenticadoId != null) {
                    // Se não tem projetos vinculados e não é admin, só vê os seus
                    predicates.add(
                        cb.equal(root.get("usuarioResponsavel").get("id"), usuarioAutenticadoId)
                    );
                }
            }
            // Se isAdmin = true, não adiciona nenhum filtro de permissão (vê todos)

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
