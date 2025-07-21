package br.uece.alunos.sisreserva.v1.dto.usuarioCargo;

import jakarta.validation.constraints.NotEmpty;

public record CriarUsuarioCargoDTO(
        @NotEmpty(message = "O ID do usuário não pode estar vazio")
        String usuarioId,

        @NotEmpty(message = "O ID do cargo não pode estar vazio")
        String cargoId
) {}
