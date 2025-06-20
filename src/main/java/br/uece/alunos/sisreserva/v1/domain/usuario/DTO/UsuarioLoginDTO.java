package br.uece.alunos.sisreserva.v1.domain.usuario.DTO;

import jakarta.validation.constraints.NotNull;

public record UsuarioLoginDTO(
        @NotNull
        String email,
        @NotNull
        String senha
) {  }
