package br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CriarEquipamentoEspacoDTO(
        @Valid
        @NotNull(message = "Os dados dos equipamentos são obrigatórios")
        List<EquipamentoDTO> equipamentos,

        @NotBlank(message = "O ID do espaço é obrigatório")
        String espacoId,

        @NotBlank(message = "O ID do usuário é obrigatório")
        String usuarioId
) {}
