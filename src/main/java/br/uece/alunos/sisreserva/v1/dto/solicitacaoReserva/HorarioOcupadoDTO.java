package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.time.LocalDateTime;

public record HorarioOcupadoDTO(
    String espacoId,
    String espacoNome,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    String usuarioSolicitante,
    String projetoNome
) {}