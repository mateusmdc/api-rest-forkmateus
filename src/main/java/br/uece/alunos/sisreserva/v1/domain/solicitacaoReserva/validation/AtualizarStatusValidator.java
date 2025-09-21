package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.validation;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import org.springframework.stereotype.Component;

@Component
public class AtualizarStatusValidator {

    /**
     * Valida se a transição de status é válida
     */
    public void validarTransicaoStatus(SolicitacaoReserva solicitacao, StatusSolicitacao novoStatus) {
        StatusSolicitacao statusAtual = solicitacao.getStatus();

        // Não permitir alterar status se já está aprovado ou recusado (regra de negócio)
        if (statusAtual == StatusSolicitacao.APROVADO && novoStatus != StatusSolicitacao.PENDENTE_AJUSTE) {
            throw new IllegalArgumentException("Não é possível alterar o status de uma solicitação aprovada, exceto para pendente de ajuste.");
        }

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