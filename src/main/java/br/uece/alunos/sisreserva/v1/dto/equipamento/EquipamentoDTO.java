package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.domain.equipamento.StatusEquipamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoDTO(
        String tombamento,
        String descricao,
        @NotNull(message = "O status do equipamento é obrigatório")
        StatusEquipamento statusEquipamento,
        @NotBlank(message = "O ID do tipo de equipamento é obrigatório")
        String tipoEquipamentoId
) {}
