package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusSolicitacaoDTO(
    @NotNull(message = "Status é obrigatório")
    StatusSolicitacao status
) {}