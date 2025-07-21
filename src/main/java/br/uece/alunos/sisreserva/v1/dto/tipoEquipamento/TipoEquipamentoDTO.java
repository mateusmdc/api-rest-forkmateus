package br.uece.alunos.sisreserva.v1.dto.tipoEquipamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TipoEquipamentoDTO(
        @NotBlank(message = "O nome é obrigatório e não pode estar em branco")
        String nome,

        @NotNull(message = "O campo isDetalhamentoObrigatorio é obrigatório")
        Boolean isDetalhamentoObrigatorio) {
}
