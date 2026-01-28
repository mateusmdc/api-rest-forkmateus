package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.specification.SolicitacaoReservaSpecification;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Caso de uso responsável por obter solicitações de reserva com filtros.
 * Aplica automaticamente filtros de permissão baseados no cargo do usuário autenticado.
 * <p>
 * Regras de visualização:
 * <ul>
 *   <li>Admin: vê todas as reservas</li>
 *   <li>Gestor: vê as suas + reservas dos espaços que gerencia + equipamentos desses espaços</li>
 *   <li>Secretaria: vê as suas + reservas dos espaços da secretaria + equipamentos desses espaços</li>
 *   <li>Usuário interno/externo: vê apenas as suas próprias reservas</li>
 * </ul>
 */
@Component
public class ObterSolicitacaoReserva {
    @Autowired
    private SolicitacaoReservaRepository solicitacaoReservaRepository;
    
    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;
    
    @Autowired
    private GestorEspacoRepository gestorEspacoRepository;
    
    @Autowired
    private SecretariaEspacoRepository secretariaEspacoRepository;
    
    @Autowired
    private EquipamentoEspacoRepository equipamentoEspacoRepository;

    /**
     * Obtém solicitações de reserva aplicando filtros de dados e permissões.
     * Automaticamente restringe a visualização baseado no cargo do usuário autenticado.
     * 
     * @param pageable Configuração de paginação
     * @param id Filtro por ID
     * @param dataInicio Filtro por data de início
     * @param dataFim Filtro por data de fim
     * @param espacoId Filtro por espaço
     * @param equipamentoId Filtro por equipamento
     * @param usuarioSolicitanteId Filtro por usuário solicitante
     * @param statusCodigo Filtro por status
     * @param projetoId Filtro por projeto
     * @return Página de solicitações de reserva
     */
    public Page<SolicitacaoReservaRetornoDTO> obterSolicitacaoReserva(
        Pageable pageable,
        String id,
        LocalDate dataInicio,
        LocalDate dataFim,
        String espacoId,
        String equipamentoId,
        String usuarioSolicitanteId,
        Integer statusCodigo,
        String projetoId
    ) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (dataInicio != null) filtros.put("dataInicio", dataInicio);
        if (dataFim != null) filtros.put("dataFim", dataFim);
        if (espacoId != null) filtros.put("espacoId", espacoId);
        if (equipamentoId != null) filtros.put("equipamentoId", equipamentoId);
        if (usuarioSolicitanteId != null) filtros.put("usuarioSolicitanteId", usuarioSolicitanteId);
        if (statusCodigo != null) filtros.put("statusCodigo", statusCodigo);
        if (projetoId != null) filtros.put("projetoId", projetoId);

        return execute(filtros, pageable).map(SolicitacaoReservaRetornoDTO::new);
    }

    /**
     * Executa a consulta aplicando filtros de dados e permissões.
     * Determina automaticamente as permissões do usuário autenticado.
     */
    private Page<SolicitacaoReserva> execute(Map<String, Object> filtros, Pageable pageable) {
        // Obter informações de permissão do usuário autenticado
        var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        boolean isAdmin = usuarioAutenticadoService.isAdmin();
        String usuarioId = usuario != null ? usuario.getId() : null;
        
        // Buscar espaços que o usuário pode gerenciar/ver
        List<String> espacosPermitidos = obterEspacosPermitidos(usuarioId, isAdmin);
        
        // Buscar equipamentos vinculados aos espaços gerenciados
        List<String> equipamentosPermitidos = obterEquipamentosPermitidos(espacosPermitidos, isAdmin);
        
        return solicitacaoReservaRepository.findAll(
            SolicitacaoReservaSpecification.byFilter(
                (String) filtros.get("id"),
                (LocalDate) filtros.get("dataInicio"),
                (LocalDate) filtros.get("dataFim"),
                (String) filtros.get("espacoId"),
                (String) filtros.get("equipamentoId"),
                (String) filtros.get("usuarioSolicitanteId"),
                (Integer) filtros.get("statusCodigo"),
                (String) filtros.get("projetoId"),
                isAdmin,
                usuarioId,
                espacosPermitidos,
                equipamentosPermitidos
            ),
            pageable
        );
    }
    
    /**
     * Obtém a lista de IDs dos espaços que o usuário tem permissão para visualizar reservas.
     * Combina espaços gerenciados (gestor) e espaços secretariados.
     * 
     * @param usuarioId ID do usuário autenticado
     * @param isAdmin Se o usuário é administrador
     * @return Lista de IDs dos espaços permitidos (vazia se for apenas usuário comum)
     */
    private List<String> obterEspacosPermitidos(String usuarioId, boolean isAdmin) {
        if (isAdmin || usuarioId == null) {
            // Admin vê tudo, não precisa filtrar por espaços
            return List.of();
        }
        
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
    
    /**
     * Obtém a lista de IDs dos equipamentos vinculados aos espaços que o usuário gerencia.
     * Permite que gestores/secretarias vejam reservas de equipamentos dos espaços que gerenciam.
     * 
     * @param espacosIds Lista de IDs dos espaços gerenciados
     * @param isAdmin Se o usuário é administrador
     * @return Lista de IDs dos equipamentos vinculados (vazia se admin ou sem espaços)
     */
    private List<String> obterEquipamentosPermitidos(List<String> espacosIds, boolean isAdmin) {
        if (isAdmin || espacosIds == null || espacosIds.isEmpty()) {
            // Admin vê tudo, não precisa filtrar por equipamentos
            // Se não tem espaços gerenciados, não tem equipamentos permitidos
            return List.of();
        }
        
        // Buscar equipamentos vinculados aos espaços gerenciados
        return equipamentoEspacoRepository.findEquipamentosIdsVinculadosAosEspacos(espacosIds);
    }
}
