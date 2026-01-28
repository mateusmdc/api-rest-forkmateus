package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation;

import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.TipoRecorrencia;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Validador de regras de negócio para solicitações de reserva.
 * 
 * <p>Valida conflitos de horários, regras de recorrência, permissões de acesso
 * e validações específicas para reservas de equipamentos.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 2.0
 */
@Slf4j
@Component
public class SolicitacaoReservaValidator {

    @Autowired
    private SolicitacaoReservaRepository repository;
    
    @Autowired
    private EspacoRepository espacoRepository;
    
    @Autowired
    private EquipamentoRepository equipamentoRepository;
    
    @Autowired
    private EquipamentoEspacoRepository equipamentoEspacoRepository;
    
    @Autowired
    private GestorEspacoRepository gestorEspacoRepository;
    
    @Autowired
    private SecretariaEspacoRepository secretariaEspacoRepository;
    
    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Valida se já existe uma solicitação de reserva aprovada para o mesmo espaço e período informado.
     * 
     * @param espacoId identificador do espaço
     * @param dataInicio data e hora de início da reserva
     * @param dataFim data e hora de fim da reserva
     * @throws IllegalArgumentException se houver conflito de horários
     */
    public void validarConflitoReserva(String espacoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        boolean existeConflito = repository.existsByEspacoIdAndPeriodoConflitanteAprovado(espacoId, dataInicio, dataFim);
        if (existeConflito) {
            throw new IllegalArgumentException("Já existe uma solicitação de reserva aprovada para este espaço no período informado.");
        }
    }

    /**
     * Valida as datas de início e fim de uma reserva.
     * 
     * <p>Verifica se:</p>
     * <ul>
     *   <li>A data de início não é anterior à data/hora atual</li>
     *   <li>A data de fim não é anterior ou igual à data de início</li>
     *   <li>As datas não são nulas</li>
     * </ul>
     * 
     * @param dataInicio data e hora de início da reserva
     * @param dataFim data e hora de fim da reserva
     * @throws IllegalArgumentException se as datas forem inválidas
     */
    public void validarDatasReserva(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("Data de início da reserva não pode ser nula");
        }

        if (dataFim == null) {
            throw new IllegalArgumentException("Data de fim da reserva não pode ser nula");
        }

        LocalDateTime agora = LocalDateTime.now();

        // Validar que a data de início não é no passado
        if (dataInicio.isBefore(agora)) {
            throw new IllegalArgumentException(
                "Não é possível criar reservas para datas passadas. " +
                "A data de início deve ser igual ou posterior à data/hora atual."
            );
        }

        // Validar que a data de fim é posterior à data de início
        if (dataFim.isBefore(dataInicio) || dataFim.isEqual(dataInicio)) {
            throw new IllegalArgumentException(
                "A data de fim da reserva deve ser posterior à data de início. " +
                "Data início: " + dataInicio + ", Data fim: " + dataFim
            );
        }
    }

    /**
     * Valida os dados de recorrência de uma reserva.
     * 
     * <p>Verifica se a data fim de recorrência foi informada quando necessária
     * e se está em um período válido.</p>
     * 
     * @param tipoRecorrencia tipo de recorrência da reserva
     * @param dataFimRecorrencia data até quando a recorrência deve se repetir
     * @throws IllegalArgumentException se os dados de recorrência forem inválidos
     */
    public void validarDadosRecorrencia(TipoRecorrencia tipoRecorrencia, LocalDateTime dataFimRecorrencia) {
        if (tipoRecorrencia == null) {
            throw new IllegalArgumentException("Tipo de recorrência não pode ser nulo");
        }

        // Se é uma reserva recorrente, data fim de recorrência é obrigatória
        if (tipoRecorrencia != TipoRecorrencia.NAO_REPETE) {
            if (dataFimRecorrencia == null) {
                throw new IllegalArgumentException(
                    "Data fim de recorrência é obrigatória para reservas recorrentes. " +
                    "Tipo de recorrência informado: " + tipoRecorrencia.getDescricao()
                );
            }

            // Validar que a data fim de recorrência não está muito distante (máximo 1 ano)
            LocalDateTime maxDataFim = LocalDateTime.now().plusYears(1);
            if (dataFimRecorrencia.isAfter(maxDataFim)) {
                throw new IllegalArgumentException(
                    "Data fim de recorrência não pode ser superior a 1 ano a partir de hoje"
                );
            }
        }
    }

    /**
     * Valida se um usuário externo tem permissão para reservar o espaço informado.
     * 
     * <p>Usuários externos só podem reservar espaços marcados como multiusuário.
     * Administradores e usuários internos não possuem essa restrição.</p>
     * 
     * @param espacoId identificador do espaço a ser reservado
     * @throws ValidationException se o usuário externo tentar reservar um espaço não-multiusuário
     */
    public void validarPermissaoUsuarioExterno(String espacoId) {
        // Verifica se o usuário autenticado é externo e não é admin
        if (!usuarioAutenticadoService.deveAplicarRestricoesMultiusuario()) {
            return; // Usuário não tem restrições
        }

        // Busca o espaço para verificar se é multiusuário
        var espaco = espacoRepository.findById(espacoId)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado com o ID: " + espacoId));

        // Se o espaço não é multiusuário, usuário externo não pode reservar
        if (!espaco.getMultiusuario()) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            
            // Log de auditoria: registra tentativa de acesso negado
            if (usuario != null) {
                log.warn("[AUDIT] ACESSO_NEGADO - Usuário externo '{}' (ID: {}) tentou reservar espaço não-multiusuário '{}' (ID: {}, multiusuario: false)",
                        usuario.getEmail(), usuario.getId(), espaco.getNome(), espaco.getId());
            }
            
            throw new ValidationException(
                "Usuários externos só podem solicitar reservas para espaços multiusuário. " +
                "O espaço selecionado não está disponível para usuários externos."
            );
        }
        
        // Log de auditoria: registra validação bem-sucedida
        var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        if (usuario != null) {
            log.info("[AUDIT] VALIDACAO_SUCESSO - Usuário externo '{}' (ID: {}) validado para reservar espaço multiusuário '{}' (ID: {})",
                    usuario.getEmail(), usuario.getId(), espaco.getNome(), espaco.getId());
        }
    }

    /**
     * Valida se exatamente um tipo de reserva foi especificado (espaço OU equipamento).
     * 
     * @param espacoId identificador do espaço
     * @param equipamentoId identificador do equipamento
     * @throws ValidationException se ambos ou nenhum estiverem preenchidos
     */
    public void validarTipoReserva(String espacoId, String equipamentoId) {
        boolean temEspaco = espacoId != null && !espacoId.isBlank();
        boolean temEquipamento = equipamentoId != null && !equipamentoId.isBlank();
        
        if (!temEspaco && !temEquipamento) {
            throw new ValidationException(
                "É necessário especificar o espaço OU o equipamento a ser reservado"
            );
        }
        
        if (temEspaco && temEquipamento) {
            throw new ValidationException(
                "Não é possível reservar espaço e equipamento na mesma solicitação. " +
                "Por favor, crie solicitações separadas para cada tipo de reserva"
            );
        }
    }

    /**
     * Valida se o equipamento existe e está vinculado a um espaço ativo.
     * Equipamentos só podem ser reservados se estiverem alocados a um espaço.
     * 
     * @param equipamentoId identificador do equipamento
     * @return ID do espaço ao qual o equipamento está vinculado
     * @throws ValidationException se o equipamento não existe ou não está vinculado a um espaço
     */
    public String validarEquipamentoVinculadoAEspaco(String equipamentoId) {
        // Verifica se o equipamento existe
        var equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new ValidationException("Equipamento não encontrado com o ID: " + equipamentoId));
        
        // Verifica se o equipamento está vinculado a algum espaço (ativo)
        var equipamentoEspaco = equipamentoEspacoRepository.findByEquipamentoIdAndDataRemocaoIsNull(equipamentoId);
        
        if (equipamentoEspaco == null || equipamentoEspaco.isEmpty()) {
            log.warn("[VALIDATION] Tentativa de reservar equipamento '{}' (ID: {}) que não está vinculado a nenhum espaço",
                    equipamento.getTombamento(), equipamentoId);
            throw new ValidationException(
                "Este equipamento não está disponível para reserva. " +
                "Equipamentos só podem ser reservados quando estão vinculados a um espaço"
            );
        }
        
        String espacoId = equipamentoEspaco.get(0).getEspaco().getId();
        log.debug("[VALIDATION] Equipamento '{}' (ID: {}) vinculado ao espaço ID: {}",
                equipamento.getTombamento(), equipamentoId, espacoId);
        
        return espacoId;
    }

    /**
     * Valida conflito de reserva para equipamento específico.
     * 
     * @param equipamentoId identificador do equipamento
     * @param dataInicio data e hora de início da reserva
     * @param dataFim data e hora de fim da reserva
     * @throws ValidationException se houver conflito de horários
     */
    public void validarConflitoReservaEquipamento(String equipamentoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        boolean existeConflito = repository.existsByEquipamentoIdAndPeriodoConflitanteAprovado(
                equipamentoId, dataInicio, dataFim);
        
        if (existeConflito) {
            log.warn("[VALIDATION] Conflito de reserva para equipamento ID: {} no período {} a {}",
                    equipamentoId, dataInicio, dataFim);
            throw new ValidationException(
                "Já existe uma solicitação de reserva aprovada para este equipamento no período informado"
            );
        }
    }

    /**
     * Valida permissões do usuário para reservar equipamento.
     * Usuários externos só podem reservar equipamentos marcados como multiusuário.
     * 
     * @param equipamentoId ID do equipamento a ser reservado
     * @throws ValidationException se o usuário não tem permissão
     */
    public void validarPermissaoUsuarioExternoEquipamento(String equipamentoId) {
        // Verifica se o usuário autenticado é externo e não é admin
        if (!usuarioAutenticadoService.deveAplicarRestricoesMultiusuario()) {
            return; // Usuário não tem restrições
        }

        // Busca o equipamento para verificar se é multiusuário
        var equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new ValidationException("Equipamento não encontrado com o ID: " + equipamentoId));

        // Se o equipamento não é multiusuário, usuário externo não pode reservar
        if (!equipamento.getMultiusuario()) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            
            // Log de auditoria: registra tentativa de acesso negado
            if (usuario != null) {
                log.warn("[AUDIT] ACESSO_NEGADO - Usuário externo '{}' (ID: {}) tentou reservar equipamento não-multiusuário '{}' (ID: {}, multiusuario: false)",
                        usuario.getEmail(), usuario.getId(), equipamento.getTombamento(), equipamento.getId());
            }
            
            throw new ValidationException(
                "Usuários externos só podem solicitar reservas para equipamentos multiusuário. " +
                "O equipamento selecionado não está disponível para usuários externos."
            );
        }
        
        // Log de auditoria: registra validação bem-sucedida
        var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        if (usuario != null) {
            log.info("[AUDIT] VALIDACAO_SUCESSO - Usuário externo '{}' (ID: {}) validado para reservar equipamento multiusuário '{}' (ID: {})",
                    usuario.getEmail(), usuario.getId(), equipamento.getTombamento(), equipamento.getId());
        }
    }

    /**
     * Valida se o usuário tem permissão para gerenciar reservas de equipamentos do espaço.
     * Gestores e secretários de um espaço podem gerenciar reservas de equipamentos daquele espaço.
     * 
     * @param usuarioId ID do usuário
     * @param espacoId ID do espaço
     * @param operacao Descrição da operação sendo realizada (para logs)
     * @throws ValidationException se o usuário não tem permissão
     */
    public void validarPermissaoGerenciamentoEquipamento(String usuarioId, String espacoId, String operacao) {
        // Admin sempre tem permissão
        if (usuarioAutenticadoService.isAdmin()) {
            return;
        }
        
        // Verifica se é gestor do espaço
        boolean isGestor = gestorEspacoRepository.existsByUsuarioGestorIdAndEspacoIdAndEstaAtivoTrue(
                usuarioId, espacoId);
        
        // Verifica se é secretário do espaço  
        boolean isSecretaria = secretariaEspacoRepository.existsByUsuarioSecretariaIdAndEspacoIdAndEstaAtivoTrue(
                usuarioId, espacoId);
        
        if (!isGestor && !isSecretaria) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            log.warn("[AUDIT] ACESSO_NEGADO - Usuário '{}' (ID: {}) tentou {} sem ser gestor/secretário do espaço ID: {}",
                    usuario != null ? usuario.getEmail() : "desconhecido", usuarioId, operacao, espacoId);
            throw new ValidationException(
                "Você não tem permissão para gerenciar reservas de equipamentos deste espaço. " +
                "Apenas gestores e secretários do espaço podem realizar esta operação"
            );
        }
        
        log.info("[AUDIT] PERMISSAO_VALIDADA - Usuário ID: {} autorizado para {} no espaço ID: {} (Gestor: {}, Secretaria: {})",
                usuarioId, operacao, espacoId, isGestor, isSecretaria);
    }

    /**
     * Valida se o usuário já possui uma solicitação de reserva ativa para o mesmo espaço ou equipamento no período.
     * 
     * <p>Evita que um usuário crie múltiplas solicitações de reserva para o mesmo recurso (espaço ou equipamento)
     * no mesmo intervalo de horários. Considera conflito quando já existe uma solicitação com status PENDENTE 
     * ou APROVADO que sobrepõe o período informado para o mesmo espaço ou equipamento.</p>
     * 
     * <p>Permite que o usuário crie solicitações simultâneas para diferentes espaços/equipamentos.</p>
     * 
     * @param usuarioId ID do usuário solicitante
     * @param espacoId ID do espaço (null se for reserva de equipamento)
     * @param equipamentoId ID do equipamento (null se for reserva de espaço)
     * @param dataInicio data e hora de início da nova reserva
     * @param dataFim data e hora de fim da nova reserva
     * @throws ValidationException se o usuário já possui solicitação ativa para o mesmo recurso no período
     */
    public void validarSolicitacaoDuplicada(String usuarioId, String espacoId, String equipamentoId, 
                                            LocalDateTime dataInicio, LocalDateTime dataFim) {
        boolean existeSolicitacaoDuplicada = repository.existsByUsuarioIdAndPeriodoConflitante(
                usuarioId, espacoId, equipamentoId, dataInicio, dataFim);
        
        if (existeSolicitacaoDuplicada) {
            var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
            String recurso = espacoId != null ? "espaço ID: " + espacoId : "equipamento ID: " + equipamentoId;
            
            log.warn("[VALIDATION] Usuário '{}' (ID: {}) tentou criar solicitação duplicada para {} no período {} a {}",
                    usuario != null ? usuario.getEmail() : "desconhecido", 
                    usuarioId, recurso, dataInicio, dataFim);
            
            throw new ValidationException(
                "Você já possui uma solicitação de reserva ativa para este " + 
                (espacoId != null ? "espaço" : "equipamento") + " no período informado. " +
                "Não é permitido criar múltiplas solicitações para o mesmo recurso no mesmo horário."
            );
        }
        
        String recurso = espacoId != null ? "espaço ID: " + espacoId : "equipamento ID: " + equipamentoId;
        log.debug("[VALIDATION] Nenhuma solicitação duplicada encontrada para usuário ID: {} e {} no período {} a {}",
                usuarioId, recurso, dataInicio, dataFim);
    }
}