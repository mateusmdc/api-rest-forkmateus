package br.uece.alunos.sisreserva.v1.dto.gestorEspaco;

import jakarta.validation.constraints.NotEmpty;

public record GestorEspacoDTO(
        @NotEmpty(message = "O campo usuarioGestorId não pode ser vazio")
        String usuarioGestorId,

        @NotEmpty(message = "O campo espacoId não pode ser vazio")
        String espacoId
) {}
