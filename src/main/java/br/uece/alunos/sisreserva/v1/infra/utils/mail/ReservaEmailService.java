package br.uece.alunos.sisreserva.v1.infra.utils.mail;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.dto.utils.MailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Serviço responsável por enviar notificações por email relacionadas às solicitações de reserva.
 * 
 * <p>Gerencia o envio de emails para:</p>
 * <ul>
 *   <li>Gestores de espaço quando uma nova reserva é solicitada</li>
 *   <li>Solicitantes quando o status da reserva é alterado</li>
 * </ul>
 * 
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaEmailService {
    
    private final MailSenderMime mailSenderMime;
    private final GestorEspacoRepository gestorEspacoRepository;
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
    
    /**
     * Envia notificação para os gestores do espaço quando uma nova reserva é solicitada.
     * 
     * <p>Método assíncrono que busca todos os gestores ativos do espaço e envia
     * um email informando sobre a nova solicitação de reserva.</p>
     * 
     * @param solicitacao a solicitação de reserva criada
     */
    @Async
    public void notificarGestoresSobreNovaSolicitacao(SolicitacaoReserva solicitacao) {
        try {
            // Buscar gestores ativos do espaço com JOIN FETCH para evitar LazyInitializationException
            List<String> emailsGestores = gestorEspacoRepository
                    .findGestoresAtivosComUsuarioByEspacoId(solicitacao.getEspaco().getId())
                    .stream()
                    .map(gestor -> gestor.getUsuarioGestor().getEmail())
                    .distinct()
                    .toList();
            
            if (emailsGestores.isEmpty()) {
                log.warn("Nenhum gestor ativo encontrado para o espaço: {}", solicitacao.getEspaco().getNome());
                return;
            }
            
            // Criar conteúdo do email
            String assunto = String.format("[SISRESERVA] Nova Solicitação de Reserva - %s", 
                    solicitacao.getEspaco().getNome());
            
            String corpo = construirEmailNovaSolicitacao(solicitacao);
            
            // Enviar email para cada gestor
            for (String emailGestor : emailsGestores) {
                MailDTO mailDTO = new MailDTO(assunto, emailGestor, corpo);
                mailSenderMime.sendMail(mailDTO);
                log.info("Email enviado para gestor: {} sobre solicitação: {}", emailGestor, solicitacao.getId());
            }
            
        } catch (Exception e) {
            log.error("Erro ao enviar notificação para gestores sobre solicitação: {}", solicitacao.getId(), e);
        }
    }
    
    /**
     * Envia notificação para o solicitante quando o status da reserva é alterado.
     * 
     * <p>Método assíncrono que envia um email ao usuário que solicitou a reserva
     * informando sobre a mudança de status (aprovação, cancelamento, etc.).</p>
     * 
     * @param solicitacao a solicitação de reserva com status atualizado
     * @param statusAnterior o status anterior da solicitação
     */
    @Async
    public void notificarSolicitanteSobreAlteracaoStatus(
            SolicitacaoReserva solicitacao, 
            StatusSolicitacao statusAnterior) {
        try {
            String emailSolicitante = solicitacao.getUsuarioSolicitante().getEmail();
            
            // Criar conteúdo do email baseado no novo status
            String assunto = String.format("[SISRESERVA] Atualização de Reserva - %s", 
                    solicitacao.getEspaco().getNome());
            
            String corpo = construirEmailAlteracaoStatus(solicitacao, statusAnterior);
            
            // Enviar email
            MailDTO mailDTO = new MailDTO(assunto, emailSolicitante, corpo);
            mailSenderMime.sendMail(mailDTO);
            
            log.info("Email enviado para solicitante: {} sobre alteração de status da reserva: {}", 
                    emailSolicitante, solicitacao.getId());
            
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de alteração de status para solicitação: {}", 
                    solicitacao.getId(), e);
        }
    }
    
    /**
     * Constrói o corpo do email para notificação de nova solicitação aos gestores.
     * 
     * @param solicitacao a solicitação de reserva
     * @return corpo do email em formato texto
     */
    private String construirEmailNovaSolicitacao(SolicitacaoReserva solicitacao) {
        StringBuilder corpo = new StringBuilder();
        
        corpo.append("Olá,\n\n");
        corpo.append("Uma nova solicitação de reserva foi realizada para um espaço sob sua gestão.\n\n");
        corpo.append("DETALHES DA SOLICITAÇÃO:\n");
        corpo.append("─────────────────────────────────────────\n\n");
        corpo.append("Espaço: ").append(solicitacao.getEspaco().getNome()).append("\n");
        corpo.append("Solicitante: ").append(solicitacao.getUsuarioSolicitante().getNome()).append("\n");
        corpo.append("Email do Solicitante: ").append(solicitacao.getUsuarioSolicitante().getEmail()).append("\n");
        corpo.append("Data/Hora Início: ").append(solicitacao.getDataInicio().format(DATE_TIME_FORMATTER)).append("\n");
        corpo.append("Data/Hora Fim: ").append(solicitacao.getDataFim().format(DATE_TIME_FORMATTER)).append("\n");
        corpo.append("Status Atual: ").append(obterDescricaoStatus(solicitacao.getStatus())).append("\n");
        
        if (solicitacao.getProjeto() != null) {
            corpo.append("Projeto Vinculado: ").append(solicitacao.getProjeto().getNome()).append("\n");
        }
        
        if (solicitacao.getTipoRecorrencia() != null && 
            solicitacao.getTipoRecorrencia().getCodigo() != 0) {
            corpo.append("\nRECORRÊNCIA:\n");
            corpo.append("Tipo: ").append(solicitacao.getTipoRecorrencia().getDescricao()).append("\n");
            if (solicitacao.getDataFimRecorrencia() != null) {
                corpo.append("Repete até: ").append(solicitacao.getDataFimRecorrencia().format(DATE_TIME_FORMATTER)).append("\n");
            }
        }
        
        corpo.append("\n─────────────────────────────────────────\n\n");
        corpo.append("Por favor, acesse o sistema para aprovar ou recusar esta solicitação.\n\n");
        corpo.append("Atenciosamente,\n");
        corpo.append("Sistema de Reservas - UECE");
        
        return corpo.toString();
    }
    
    /**
     * Constrói o corpo do email para notificação de alteração de status ao solicitante.
     * 
     * @param solicitacao a solicitação de reserva
     * @param statusAnterior o status anterior da solicitação
     * @return corpo do email em formato texto
     */
    private String construirEmailAlteracaoStatus(
            SolicitacaoReserva solicitacao, 
            StatusSolicitacao statusAnterior) {
        StringBuilder corpo = new StringBuilder();
        
        corpo.append("Olá, ").append(solicitacao.getUsuarioSolicitante().getNome()).append(",\n\n");
        
        // Mensagem personalizada baseada no novo status
        switch (solicitacao.getStatus()) {
            case APROVADO:
                corpo.append("Sua solicitação de reserva foi APROVADA! 🎉\n\n");
                break;
            case RECUSADO:
                corpo.append("Sua solicitação de reserva foi RECUSADA.\n\n");
                break;
            case PENDENTE_AJUSTE:
                corpo.append("Sua solicitação de reserva está PENDENTE DE AJUSTE.\n\n");
                break;
            default:
                corpo.append("O status da sua solicitação de reserva foi atualizado.\n\n");
        }
        
        corpo.append("DETALHES DA RESERVA:\n");
        corpo.append("─────────────────────────────────────────\n\n");
        corpo.append("Espaço: ").append(solicitacao.getEspaco().getNome()).append("\n");
        corpo.append("Data/Hora Início: ").append(solicitacao.getDataInicio().format(DATE_TIME_FORMATTER)).append("\n");
        corpo.append("Data/Hora Fim: ").append(solicitacao.getDataFim().format(DATE_TIME_FORMATTER)).append("\n");
        corpo.append("Status Anterior: ").append(obterDescricaoStatus(statusAnterior)).append("\n");
        corpo.append("Status Atual: ").append(obterDescricaoStatus(solicitacao.getStatus())).append("\n");
        
        if (solicitacao.getProjeto() != null) {
            corpo.append("Projeto: ").append(solicitacao.getProjeto().getNome()).append("\n");
        }
        
        if (solicitacao.getTipoRecorrencia() != null && 
            solicitacao.getTipoRecorrencia().getCodigo() != 0) {
            corpo.append("\nRECORRÊNCIA:\n");
            corpo.append("Tipo: ").append(solicitacao.getTipoRecorrencia().getDescricao()).append("\n");
            if (solicitacao.getDataFimRecorrencia() != null) {
                corpo.append("Repete até: ").append(solicitacao.getDataFimRecorrencia().format(DATE_TIME_FORMATTER)).append("\n");
            }
        }
        
        corpo.append("\n─────────────────────────────────────────\n\n");
        
        // Mensagem adicional baseada no status
        if (solicitacao.getStatus() == StatusSolicitacao.APROVADO) {
            corpo.append("Sua reserva está confirmada! Compareça no horário agendado.\n\n");
        } else if (solicitacao.getStatus() == StatusSolicitacao.RECUSADO) {
            corpo.append("Caso tenha dúvidas, entre em contato com os gestores do espaço.\n\n");
        } else if (solicitacao.getStatus() == StatusSolicitacao.PENDENTE_AJUSTE) {
            corpo.append("Por favor, entre em contato com os gestores para maiores informações.\n\n");
        }
        
        corpo.append("Atenciosamente,\n");
        corpo.append("Sistema de Reservas - UECE");
        
        return corpo.toString();
    }
    
    /**
     * Obtém a descrição legível do status da solicitação.
     * 
     * @param status o status da solicitação
     * @return descrição do status
     */
    private String obterDescricaoStatus(StatusSolicitacao status) {
        return switch (status) {
            case PENDENTE -> "Pendente";
            case APROVADO -> "Aprovado";
            case RECUSADO -> "Recusado";
            case PENDENTE_AJUSTE -> "Pendente de Ajuste";
        };
    }
}
