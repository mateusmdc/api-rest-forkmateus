package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import java.time.LocalDateTime;

public record SolicitacaoReservaRetornoDTO(
    String id,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    String espacoId,
    String usuarioSolicitanteId,
    Integer status,
    String projetoId,
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
            solicitacaoReserva.getCreatedAt(),
            solicitacaoReserva.getUpdatedAt()
        );
    }
}