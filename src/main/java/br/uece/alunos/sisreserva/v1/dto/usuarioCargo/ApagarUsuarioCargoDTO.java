package br.uece.alunos.sisreserva.v1.dto.usuarioCargo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ApagarUsuarioCargoDTO(@NotNull @NotEmpty String usuarioId, @NotEmpty @NotNull String cargoId) {
}
