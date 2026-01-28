package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.secretariaEspaco.SecretariaEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validador de regras de negócio para atualização de status de solicitações de reserva.
 * 
 * <p>Valida transições de status e permissões de acesso baseadas em cargo e relação
 * com o espaço/equipamento da reserva.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 2.0
 */
@Slf4j
@Component
public class AtualizarStatusValidator {

    @Autowired
    private GestorEspacoRepository gestorEspacoRepository;

    @Autowired
    private SecretariaEspacoRepository secretariaEspacoRepository;

    @Autowired
    private EquipamentoEspacoRepository equipamentoEspacoRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    /**
     * Valida se o usuário tem permissão para atualizar o status da solicitação.
     * 
     * <p>Regras de permissão:</p>
     * <ul>
     *   <li>ADMIN: pode atualizar para qualquer status</li>
     *   <li>GESTOR/SECRETARIA do espaço: pode atualizar para qualquer status</li>
     *   <li>USUARIO SOLICITANTE: pode atualizar APENAS para CANCELADO e APENAS suas próprias solicitações</li>
     *   <li>Outros usuários: sem permissão</li>
     * </ul>
     * 
     * @param solicitacao a solicitação de reserva
     * @param novoStatus o novo status desejado
     * @throws ValidationException se o usuário não tem permissão
     */
    public void validarPermissaoParaAtualizarStatus(SolicitacaoReserva solicitacao, StatusSolicitacao novoStatus) {
        var usuarioAutenticado = usuarioAutenticadoService.getUsuarioAutenticado();
        
        if (usuarioAutenticado == null) {
            log.error("[AUDIT] ACESSO_NEGADO - Tentativa de atualizar status sem usuário autenticado. Solicitação ID: {}",
                    solicitacao.getId());
            throw new ValidationException("Usuário não autenticado.");
        }
        
        String usuarioId = usuarioAutenticado.getId();
        
        // Admin sempre tem permissão
        if (usuarioAutenticadoService.isAdmin()) {
            log.info("[AUDIT] PERMISSAO_VALIDADA - Admin '{}' (ID: {}) autorizado para atualizar status da solicitação ID: {} para {}",
                    usuarioAutenticado.getEmail(), usuarioId, solicitacao.getId(), novoStatus);
            return;
        }
        
        // Determinar se é reserva de espaço ou equipamento
        Espaco espaco = solicitacao.getEspaco();
        Equipamento equipamento = solicitacao.getEquipamento();
        String espacoId = null;
        
        // Se for reserva de equipamento, obter o espaço vinculado
        if (equipamento != null) {
            var equipamentoEspaco = equipamentoEspacoRepository.findByEquipamentoIdAndDataRemocaoIsNull(equipamento.getId());
            if (equipamentoEspaco != null && !equipamentoEspaco.isEmpty()) {
                espacoId = equipamentoEspaco.get(0).getEspaco().getId();
            }
        } else if (espaco != null) {
            espacoId = espaco.getId();
        }
        
        // Verificar se é gestor ou secretária do espaço
        boolean isGestor = false;
        boolean isSecretaria = false;
        
        if (espacoId != null) {
            isGestor = gestorEspacoRepository.existsByUsuarioGestorIdAndEspacoIdAndEstaAtivoTrue(
                    usuarioId, espacoId);
            isSecretaria = secretariaEspacoRepository.existsByUsuarioSecretariaIdAndEspacoIdAndEstaAtivoTrue(
                    usuarioId, espacoId);
        }
        
        // Gestor ou secretária tem permissão total
        if (isGestor || isSecretaria) {
            log.info("[AUDIT] PERMISSAO_VALIDADA - Usuário '{}' (ID: {}) autorizado como {} do espaço ID: {} para atualizar solicitação ID: {} para {}",
                    usuarioAutenticado.getEmail(), usuarioId, 
                    isGestor ? "gestor" : "secretária",
                    espacoId, solicitacao.getId(), novoStatus);
            return;
        }
        
        // Verificar se é o próprio solicitante tentando cancelar
        boolean isSolicitante = solicitacao.getUsuarioSolicitante() != null && 
                                usuarioId.equals(solicitacao.getUsuarioSolicitante().getId());
        
        if (isSolicitante && novoStatus == StatusSolicitacao.CANCELADO) {
            log.info("[AUDIT] PERMISSAO_VALIDADA - Usuário solicitante '{}' (ID: {}) autorizado para cancelar sua própria solicitação ID: {}",
                    usuarioAutenticado.getEmail(), usuarioId, solicitacao.getId());
            return;
        }
        
        // Nenhuma permissão encontrada - negar acesso
        String nomeRecurso = espaco != null ? espaco.getNome() : 
                           (equipamento != null ? equipamento.getDescricao() : "desconhecido");
        
        log.warn("[AUDIT] ACESSO_NEGADO - Usuário '{}' (ID: {}) tentou atualizar status da solicitação ID: {} para {} sem permissão. Recurso: {}",
                usuarioAutenticado.getEmail(), usuarioId, solicitacao.getId(), novoStatus, nomeRecurso);
        
        if (isSolicitante) {
            throw new ValidationException(
                "Você pode apenas cancelar suas próprias solicitações. " +
                "Apenas administradores, gestores ou secretária do espaço/equipamento podem alterar para outros status."
            );
        } else {
            throw new ValidationException(
                "Você não tem permissão para alterar o status desta solicitação. " +
                "Apenas administradores, gestores ou secretária do espaço/equipamento podem realizar esta operação."
            );
        }
    }

    /**
     * Valida se a transição de status é válida.
     * 
     * <p>Regras de transição:</p>
     * <ul>
     *   <li>APROVADO: só pode ir para PENDENTE_AJUSTE ou CANCELADO</li>
     *   <li>RECUSADO: só pode ir para PENDENTE</li>
     *   <li>CANCELADO: é um estado final, não permite alterações</li>
     *   <li>PENDENTE e PENDENTE_AJUSTE: podem ir para qualquer status</li>
     * </ul>
     */
    public void validarTransicaoStatus(SolicitacaoReserva solicitacao, StatusSolicitacao novoStatus) {
        StatusSolicitacao statusAtual = solicitacao.getStatus();

        // Não permitir alterar status de uma solicitação cancelada
        if (statusAtual == StatusSolicitacao.CANCELADO) {
            throw new IllegalArgumentException("Não é possível alterar o status de uma solicitação cancelada.");
        }

        // Não permitir alterar status se já está aprovado (exceto para pendente de ajuste ou cancelado)
        if (statusAtual == StatusSolicitacao.APROVADO && 
            novoStatus != StatusSolicitacao.PENDENTE_AJUSTE && 
            novoStatus != StatusSolicitacao.CANCELADO) {
            throw new IllegalArgumentException("Não é possível alterar o status de uma solicitação aprovada, exceto para pendente de ajuste ou cancelado.");
        }

        // Não permitir alterar status se já está recusado (exceto para pendente)
        if (statusAtual == StatusSolicitacao.RECUSADO && novoStatus != StatusSolicitacao.PENDENTE) {
            throw new IllegalArgumentException("Não é possível alterar o status de uma solicitação recusada, exceto para pendente.");
        }

        // Não permitir definir o mesmo status
        if (statusAtual == novoStatus) {
            throw new IllegalArgumentException("A solicitação já possui o status informado.");
        }
    }

    /**
     * Valida se o status é válido para atualização manual
     */
    public void validarStatusPermitido(StatusSolicitacao status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }
        // Todos os status são permitidos para atualização manual
        // Adicione regras específicas se necessário
    }
}