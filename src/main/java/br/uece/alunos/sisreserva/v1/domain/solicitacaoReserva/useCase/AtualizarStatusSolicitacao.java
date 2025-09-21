package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation.AtualizarStatusValidator;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.AtualizarStatusSolicitacaoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AtualizarStatusSolicitacao {

    @Autowired
    private SolicitacaoReservaRepository repository;

    @Autowired
    private AtualizarStatusValidator validator;

    public SolicitacaoReservaRetornoDTO atualizarStatus(String solicitacaoId, AtualizarStatusSolicitacaoDTO data) {
        // Buscar a solicitação
        SolicitacaoReserva solicitacao = repository.findById(solicitacaoId)
            .orElseThrow(() -> new IllegalArgumentException("Solicitação de reserva não encontrada com ID: " + solicitacaoId));

        // Validar o novo status
        validator.validarStatusPermitido(data.status());

        // Validar a transição de status
        validator.validarTransicaoStatus(solicitacao, data.status());

        // Atualizar o status
        solicitacao.setStatus(data.status());
        solicitacao.setUpdatedAt(LocalDateTime.now());

        // Salvar as alterações
        SolicitacaoReserva solicitacaoAtualizada = repository.save(solicitacao);

        // Retornar DTO de resposta seguindo o padrão do projeto
        return new SolicitacaoReservaRetornoDTO(solicitacaoAtualizada);
    }
}