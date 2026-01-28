package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation.AtualizarStatusValidator;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.AtualizarStatusSolicitacaoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.utils.mail.ReservaEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Caso de uso para atualização de status de solicitações de reserva.
 * 
 * <p>Gerencia a transição de status das solicitações e implementa regras de negócio
 * relacionadas, como a recusa automática de solicitações conflitantes quando uma
 * reserva é aprovada.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
@Component
@Slf4j
public class AtualizarStatusSolicitacao {

    @Autowired
    private SolicitacaoReservaRepository repository;

    @Autowired
    private AtualizarStatusValidator validator;

    @Autowired
    private ReservaEmailService reservaEmailService;

    /**
     * Atualiza o status de uma solicitação de reserva.
     * 
     * <p>Quando uma reserva é aprovada, este método automaticamente recusa todas as
     * solicitações pendentes que conflitam com o mesmo intervalo de tempo, evitando
     * que gestores tenham que recusar manualmente cada solicitação conflitante.</p>
     * 
     * @param solicitacaoId ID da solicitação a ser atualizada
     * @param data dados da atualização contendo o novo status
     * @return DTO com os dados da solicitação atualizada
     * @throws IllegalArgumentException se a solicitação não for encontrada
     */
    public SolicitacaoReservaRetornoDTO atualizarStatus(String solicitacaoId, AtualizarStatusSolicitacaoDTO data) {
        // Buscar a solicitação com relações carregadas
        SolicitacaoReserva solicitacao = repository.findByIdWithRelations(solicitacaoId)
            .orElseThrow(() -> new IllegalArgumentException("Solicitação de reserva não encontrada com ID: " + solicitacaoId));

        // Capturar o status anterior para notificação
        StatusSolicitacao statusAnterior = solicitacao.getStatus();

        // Validar o novo status
        validator.validarStatusPermitido(data.status());

        // Validar a transição de status
        validator.validarTransicaoStatus(solicitacao, data.status());

        // Atualizar o status
        solicitacao.setStatus(data.status());
        solicitacao.setUpdatedAt(LocalDateTime.now());

        // Salvar as alterações
        SolicitacaoReserva solicitacaoAtualizada = repository.save(solicitacao);

        // Se a solicitação foi aprovada, recusar automaticamente outras solicitações conflitantes
        if (data.status() == StatusSolicitacao.APROVADO) {
            recusarSolicitacoesConflitantes(solicitacaoAtualizada);
        }

        // Recarregar com relações para enviar notificação (evitar LazyInitializationException)
        var solicitacaoComRelacoes = repository.findByIdWithRelations(solicitacaoAtualizada.getId())
                .orElse(solicitacaoAtualizada);

        // Enviar notificação para o solicitante sobre a mudança de status
        reservaEmailService.notificarSolicitanteSobreAlteracaoStatus(solicitacaoComRelacoes, statusAnterior);

        // Retornar DTO de resposta seguindo o padrão do projeto
        return new SolicitacaoReservaRetornoDTO(solicitacaoAtualizada);
    }

    /**
     * Recusa automaticamente todas as solicitações pendentes que conflitam com a reserva aprovada.
     * 
     * <p>Este método busca todas as solicitações pendentes para o mesmo espaço ou equipamento
     * que possuem sobreposição de horários com a reserva aprovada, atualiza o status delas
     * para RECUSADO e envia notificações por email aos solicitantes.</p>
     * 
     * @param solicitacaoAprovada a solicitação que foi aprovada
     */
    private void recusarSolicitacoesConflitantes(SolicitacaoReserva solicitacaoAprovada) {
        try {
            // Determinar se é reserva de espaço ou equipamento
            String espacoId = solicitacaoAprovada.getEspaco() != null 
                ? solicitacaoAprovada.getEspaco().getId() 
                : null;
            String equipamentoId = solicitacaoAprovada.getEquipamento() != null 
                ? solicitacaoAprovada.getEquipamento().getId() 
                : null;

            // Buscar solicitações pendentes que conflitam com o período aprovado
            List<SolicitacaoReserva> solicitacoesConflitantes = repository.findSolicitacoesPendentesConflitantes(
                solicitacaoAprovada.getId(),
                espacoId,
                equipamentoId,
                solicitacaoAprovada.getDataInicio(),
                solicitacaoAprovada.getDataFim()
            );

            if (solicitacoesConflitantes.isEmpty()) {
                log.info("[RECUSA_AUTOMATICA] Nenhuma solicitação conflitante encontrada para a reserva aprovada ID: {}",
                        solicitacaoAprovada.getId());
                return;
            }

            log.info("[RECUSA_AUTOMATICA] Encontradas {} solicitações conflitantes para recusar automaticamente. Reserva aprovada ID: {}",
                    solicitacoesConflitantes.size(), solicitacaoAprovada.getId());

            // Recusar cada solicitação conflitante
            for (SolicitacaoReserva solicitacaoConflitante : solicitacoesConflitantes) {
                StatusSolicitacao statusAnterior = solicitacaoConflitante.getStatus();
                
                // Atualizar status para RECUSADO
                solicitacaoConflitante.setStatus(StatusSolicitacao.RECUSADO);
                solicitacaoConflitante.setUpdatedAt(LocalDateTime.now());
                
                // Salvar a alteração
                repository.save(solicitacaoConflitante);
                
                log.info("[RECUSA_AUTOMATICA] Solicitação ID: {} recusada automaticamente. Usuário: {} ({}), Período: {} a {}",
                        solicitacaoConflitante.getId(),
                        solicitacaoConflitante.getUsuarioSolicitante().getNome(),
                        solicitacaoConflitante.getUsuarioSolicitante().getEmail(),
                        solicitacaoConflitante.getDataInicio(),
                        solicitacaoConflitante.getDataFim());
                
                // Enviar notificação de recusa ao solicitante
                try {
                    reservaEmailService.notificarRecusaAutomatica(solicitacaoConflitante, solicitacaoAprovada);
                } catch (Exception e) {
                    log.error("[RECUSA_AUTOMATICA] Erro ao enviar email de notificação para usuário {} sobre recusa da solicitação ID: {}",
                            solicitacaoConflitante.getUsuarioSolicitante().getEmail(),
                            solicitacaoConflitante.getId(), e);
                }
            }

            log.info("[RECUSA_AUTOMATICA] Processo concluído. Total de solicitações recusadas: {}",
                    solicitacoesConflitantes.size());

        } catch (Exception e) {
            log.error("[RECUSA_AUTOMATICA] Erro ao processar recusa automática de solicitações conflitantes para reserva ID: {}",
                    solicitacaoAprovada.getId(), e);
            // Não propaga a exceção para não afetar a aprovação da reserva principal
        }
    }
}