package br.uece.alunos.sisreserva.v1.dto.usuario;

import jakarta.validation.constraints.NotEmpty;

public record UsuarioLoginInternoDTO(
        @NotEmpty(message = "O username institucional é obrigatório")
        String ldapUsername,

        @NotEmpty(message = "A senha é obrigatória")
        String senha
) {}