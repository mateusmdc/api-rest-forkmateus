package br.uece.alunos.sisreserva.v1.dto.projeto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProjetoDTO(
    @NotEmpty @NotNull
    String nome,

    @NotEmpty @NotNull
    String descricao,

    @NotNull
    LocalDate dataInicio,

    @NotNull
    LocalDate dataFim,

    @NotEmpty @NotNull
    String usuarioResponsavelId,

    @NotEmpty @NotNull
    String instituicaoId
    
) {}