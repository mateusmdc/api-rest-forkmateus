package br.uece.alunos.sisreserva.v1.dto.usuarioCargo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CriarUsuarioCargoDTO(@NotNull @NotEmpty String usuarioId, @NotNull @NotEmpty String cargoId) {
}
