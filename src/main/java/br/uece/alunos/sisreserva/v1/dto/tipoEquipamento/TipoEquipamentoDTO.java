package br.uece.alunos.sisreserva.v1.dto.tipoEquipamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TipoEquipamentoDTO(
        @NotBlank String nome,
        @NotNull Boolean isDetalhamentoObrigatorio) {
}
