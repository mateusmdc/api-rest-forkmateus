package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de retorno para solicitação de reserva.
 * Contém todas as informações da reserva incluindo dados de recorrência.
 * Uma reserva pode ser de espaço OU equipamento (campos mutuamente exclusivos).
 */
public record SolicitacaoReservaRetornoDTO(
    String id,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    String espacoId,
    String equipamentoId,
    String usuarioSolicitanteId,
    Integer status,
    String projetoId,
    Integer tipoRecorrencia,
    LocalDateTime dataFimRecorrencia,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<OcorrenciaReservaDTO> ocorrenciasMes
) {
    /** Construtor padrão sem ocorrências do mês (para listagem sem filtro de mês). */
    public SolicitacaoReservaRetornoDTO(SolicitacaoReserva solicitacaoReserva) {
        this(
            solicitacaoReserva.getId(),
            solicitacaoReserva.getDataInicio(),
            solicitacaoReserva.getDataFim(),
            solicitacaoReserva.getEspaco() != null ? solicitacaoReserva.getEspaco().getId() : null,
            solicitacaoReserva.getEquipamento() != null ? solicitacaoReserva.getEquipamento().getId() : null,
            solicitacaoReserva.getUsuarioSolicitante().getId(),
            solicitacaoReserva.getStatus().getCodigo(),
            solicitacaoReserva.getProjeto() != null ? solicitacaoReserva.getProjeto().getId() : null,
            solicitacaoReserva.getTipoRecorrencia().getCodigo(),
            solicitacaoReserva.getDataFimRecorrencia(),
            solicitacaoReserva.getCreatedAt(),
            solicitacaoReserva.getUpdatedAt(),
            null
        );
    }

    /** Construtor com ocorrências do mês calculadas (para listagem com filtro de mês). */
    public SolicitacaoReservaRetornoDTO(SolicitacaoReserva solicitacaoReserva, List<OcorrenciaReservaDTO> ocorrenciasMes) {
        this(
            solicitacaoReserva.getId(),
            solicitacaoReserva.getDataInicio(),
            solicitacaoReserva.getDataFim(),
            solicitacaoReserva.getEspaco() != null ? solicitacaoReserva.getEspaco().getId() : null,
            solicitacaoReserva.getEquipamento() != null ? solicitacaoReserva.getEquipamento().getId() : null,
            solicitacaoReserva.getUsuarioSolicitante().getId(),
            solicitacaoReserva.getStatus().getCodigo(),
            solicitacaoReserva.getProjeto() != null ? solicitacaoReserva.getProjeto().getId() : null,
            solicitacaoReserva.getTipoRecorrencia().getCodigo(),
            solicitacaoReserva.getDataFimRecorrencia(),
            solicitacaoReserva.getCreatedAt(),
            solicitacaoReserva.getUpdatedAt(),
            ocorrenciasMes
        );
    }
}