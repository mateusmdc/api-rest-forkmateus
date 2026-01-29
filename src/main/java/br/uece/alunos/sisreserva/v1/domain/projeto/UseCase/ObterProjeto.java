package br.uece.alunos.sisreserva.v1.domain.projeto.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import br.uece.alunos.sisreserva.v1.domain.projeto.ProjetoRepository;
import br.uece.alunos.sisreserva.v1.domain.projeto.specification.ProjetoSpecification;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.dto.projeto.ProjetoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Caso de uso responsável por obter projetos com filtros.
 * Aplica automaticamente filtros de permissão baseados no cargo do usuário autenticado.
 * <p>
 * Regras de visualização:
 * <ul>
 *   <li>Admin: vê todos os projetos</li>
 *   <li>Gestor: vê seus projetos + projetos de reservas dos espaços/equipamentos que gerencia</li>
 *   <li>Secretaria: vê seus projetos + projetos de reservas dos espaços/equipamentos da secretaria</li>
 *   <li>Usuário interno/externo: vê apenas seus próprios projetos</li>
 * </ul>
 */
@Component
public class ObterProjeto {
    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;
    
    @Autowired
    private GestorEspacoRepository gestorEspacoRepository;
    
    @Autowired
    private SecretariaEspacoRepository secretariaEspacoRepository;
    
    @Autowired
    private EquipamentoEspacoRepository equipamentoEspacoRepository;
    
    @Autowired
    private SolicitacaoReservaRepository solicitacaoReservaRepository;
    /**
     * Obtém projetos aplicando filtros de dados e permissões.
     * Automaticamente restringe a visualização baseado no cargo do usuário autenticado.
     * 
     * @param pageable Configuração de paginação
     * @param id Filtro por ID
     * @param nome Filtro por nome
     * @param descricao Filtro por descrição
     * @param dataInicio Filtro por data de início
     * @param dataFim Filtro por data de fim
     * @param usuarioResponsavelId Filtro por usuário responsável
     * @param instituicaoId Filtro por instituição
     * @return Página de projetos
     */
    public Page<ProjetoRetornoDTO> obterProjetos(
        Pageable pageable,
        String id,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        String usuarioResponsavelId,
        String instituicaoId
    ){
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (nome != null) filtros.put("nome", nome);
        if (descricao != null) filtros.put("descricao", descricao);
        if (dataInicio != null) filtros.put("dataInicio", dataInicio);
        if (dataFim != null) filtros.put("dataFim", dataFim);
        if (usuarioResponsavelId != null) filtros.put("usuarioResponsavelId", usuarioResponsavelId);
        if (instituicaoId != null) filtros.put("instituicaoId", instituicaoId);

        return execute(filtros, pageable).map(ProjetoRetornoDTO::new);
    }
    
    /**
     * Executa a consulta aplicando filtros de dados e permissões.
     * Determina automaticamente as permissões do usuário autenticado.
     */
    private Page<Projeto> execute(Map<String, Object> filtros, Pageable pageable) {
        // Obter informações de permissão do usuário autenticado
        var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        boolean isAdmin = usuarioAutenticadoService.isAdmin();
        String usuarioId = usuario != null ? usuario.getId() : null;
        
        // Buscar projetos vinculados a reservas de espaços/equipamentos que o usuário gerencia
        List<String> projetosPermitidos = obterProjetosPermitidos(usuarioId, isAdmin);
        
        return projetoRepository.findAll(
            ProjetoSpecification.byFilter(
                (String) filtros.get("id"),
                (String) filtros.get("nome"),
                (String) filtros.get("descricao"),
                (LocalDate) filtros.get("dataInicio"),
                (LocalDate) filtros.get("dataFim"),
                (String) filtros.get("usuarioResponsavelId"),
                (String) filtros.get("instituicaoId"),
                isAdmin,
                usuarioId,
                projetosPermitidos
            ),
            pageable
        );
    }
    
    /**
     * Obtém a lista de IDs dos projetos vinculados a reservas de espaços/equipamentos gerenciados.
     * Combina projetos de reservas de espaços e equipamentos que o usuário gerencia ou está na secretaria.
     * 
     * @param usuarioId ID do usuário autenticado
     * @param isAdmin Se o usuário é administrador
     * @return Lista de IDs dos projetos permitidos (vazia se admin ou usuário comum)
     */
    private List<String> obterProjetosPermitidos(String usuarioId, boolean isAdmin) {
        if (isAdmin || usuarioId == null) {
            // Admin vê tudo, não precisa filtrar por projetos
            return List.of();
        }
        
        // Buscar espaços que o usuário gerencia ou está na secretaria
        List<String> espacosGerenciados = obterEspacosGerenciados(usuarioId);
        
        if (espacosGerenciados.isEmpty()) {
            // Usuário comum sem espaços gerenciados: só vê seus próprios projetos
            return List.of();
        }
        
        // Buscar equipamentos vinculados aos espaços gerenciados
        List<String> equipamentosVinculados = equipamentoEspacoRepository
            .findEquipamentosIdsVinculadosAosEspacos(espacosGerenciados);
        
        // Buscar projetos de reservas dos espaços gerenciados
        List<String> projetosDeEspacos = solicitacaoReservaRepository
            .findProjetosIdsVinculadosAosEspacos(espacosGerenciados);
        
        // Buscar projetos de reservas dos equipamentos vinculados
        List<String> projetosDeEquipamentos = List.of();
        if (!equipamentosVinculados.isEmpty()) {
            projetosDeEquipamentos = solicitacaoReservaRepository
                .findProjetosIdsVinculadosAosEquipamentos(equipamentosVinculados);
        }
        
        // Combinar ambas as listas (sem duplicatas)
        return Stream.concat(
            projetosDeEspacos.stream(),
            projetosDeEquipamentos.stream()
        ).distinct().toList();
    }
    
    /**
     * Obtém a lista de IDs dos espaços que o usuário gerencia ou está na secretaria.
     * 
     * @param usuarioId ID do usuário autenticado
     * @return Lista de IDs dos espaços
     */
    private List<String> obterEspacosGerenciados(String usuarioId) {
        // Buscar espaços que o usuário gerencia
        List<String> espacosGerenciados = gestorEspacoRepository
            .findEspacosIdsGerenciadosByUsuarioId(usuarioId);
        
        // Buscar espaços onde o usuário está na secretaria
        List<String> espacosSecretariados = secretariaEspacoRepository
            .findEspacosIdsSecretariadosByUsuarioId(usuarioId);
        
        // Combinar ambas as listas (sem duplicatas)
        return Stream.concat(
            espacosGerenciados.stream(),
            espacosSecretariados.stream()
        ).distinct().toList();
    }
}
