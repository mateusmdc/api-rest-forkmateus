package br.uece.alunos.sisreserva.v1.dto.gestorEspaco;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GestorEspacoDTO(@NotNull @NotEmpty String usuarioGestorId, @NotNull @NotEmpty String espacoId) {
}
