package br.uece.alunos.sisreserva.v1.dto.usuario;

import jakarta.validation.constraints.NotNull;

public record UsuarioTrocarSenhaDTO(
        @NotNull
        String email,
        @NotNull
        String senha,
        @NotNull
        String tokenMail
) {
}