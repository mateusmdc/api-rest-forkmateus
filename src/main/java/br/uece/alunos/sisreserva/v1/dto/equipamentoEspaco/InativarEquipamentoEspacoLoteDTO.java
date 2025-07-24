package br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record InativarEquipamentoEspacoLoteDTO(@NotEmpty(message = "lista de ids de equipamento espaço não pode ser vazia.")
                                               List<String> equipamentoEspacoIds,
                                               @NotBlank(message = "O ID do usuário é obrigatório")
                                               String usuarioId) {
}
