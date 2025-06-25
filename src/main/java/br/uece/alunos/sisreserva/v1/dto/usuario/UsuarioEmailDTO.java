package br.uece.alunos.sisreserva.v1.dto.usuario;

import jakarta.validation.constraints.NotNull;

public record UsuarioEmailDTO(@NotNull String email) {
}
