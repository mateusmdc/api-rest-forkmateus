package br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos;

import jakarta.validation.constraints.NotEmpty;

/**
 * DTO para criação/reativação de gestor de complexo de espaços.
 * Contém os IDs do usuário gestor e do complexo de espaços.
 */
public record GestorComplexoEspacosDTO(
        @NotEmpty(message = "O campo usuarioGestorId não pode ser vazio")
        String usuarioGestorId,

        @NotEmpty(message = "O campo complexoEspacosId não pode ser vazio")
        String complexoEspacosId
) {}
