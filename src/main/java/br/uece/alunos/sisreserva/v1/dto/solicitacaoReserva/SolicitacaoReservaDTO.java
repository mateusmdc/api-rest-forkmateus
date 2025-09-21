package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoReservaDTO(
    @NotNull
    LocalDateTime dataInicio,

    @NotNull
    LocalDateTime dataFim,

    @NotEmpty @NotNull
    String espacoId,

    @NotEmpty @NotNull
    String usuarioSolicitanteId,

    String projetoId // Optional
) {}