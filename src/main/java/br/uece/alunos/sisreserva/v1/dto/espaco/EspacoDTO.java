package br.uece.alunos.sisreserva.v1.dto.espaco;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EspacoDTO(
        @NotNull @NotEmpty
        String nome,
        String urlCnpq,
        String observacao,
        @NotNull @NotEmpty
        String departamentoId,
        @NotNull @NotEmpty
        String localizacaoId,
        @NotNull @NotEmpty
        String tipoEspacoId,
        @NotNull @NotEmpty
        String tipoAtividadeId,
        @NotNull
        Boolean precisaProjeto
) {}