package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import java.time.LocalDateTime;

/**
 * DTO de retorno para solicitação de reserva.
 * Contém todas as informações da reserva incluindo dados de recorrência.
 */
public record SolicitacaoReservaRetornoDTO(
    String id,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    String espacoId,
    String usuarioSolicitanteId,
    Integer status,
    String projetoId,
    Integer tipoRecorrencia,
    LocalDateTime dataFimRecorrencia,
    String reservaPaiId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public SolicitacaoReservaRetornoDTO(SolicitacaoReserva solicitacaoReserva) {
        this(
            solicitacaoReserva.getId(),
            solicitacaoReserva.getDataInicio(),
            solicitacaoReserva.getDataFim(),
            solicitacaoReserva.getEspaco().getId(),
            solicitacaoReserva.getUsuarioSolicitante().getId(),
            solicitacaoReserva.getStatus().getCodigo(),
            solicitacaoReserva.getProjeto() != null ? solicitacaoReserva.getProjeto().getId() : null,
            solicitacaoReserva.getTipoRecorrencia().getCodigo(),
            solicitacaoReserva.getDataFimRecorrencia(),
            solicitacaoReserva.getReservaPaiId(),
            solicitacaoReserva.getCreatedAt(),
            solicitacaoReserva.getUpdatedAt()
        );
    }
}