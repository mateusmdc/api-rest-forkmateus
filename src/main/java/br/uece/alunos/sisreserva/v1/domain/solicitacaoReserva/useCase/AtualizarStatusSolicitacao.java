package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation.AtualizarStatusValidator;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.AtualizarStatusSolicitacaoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.utils.mail.ReservaEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AtualizarStatusSolicitacao {

    @Autowired
    private SolicitacaoReservaRepository repository;

    @Autowired
    private AtualizarStatusValidator validator;

    @Autowired
    private ReservaEmailService reservaEmailService;

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

        // Recarregar com relações para enviar notificação (evitar LazyInitializationException)
        var solicitacaoComRelacoes = repository.findByIdWithRelations(solicitacaoAtualizada.getId())
                .orElse(solicitacaoAtualizada);

        // Enviar notificação para o solicitante sobre a mudança de status
        reservaEmailService.notificarSolicitanteSobreAlteracaoStatus(solicitacaoComRelacoes, statusAnterior);

        // Retornar DTO de resposta seguindo o padrão do projeto
        return new SolicitacaoReservaRetornoDTO(solicitacaoAtualizada);
    }
}