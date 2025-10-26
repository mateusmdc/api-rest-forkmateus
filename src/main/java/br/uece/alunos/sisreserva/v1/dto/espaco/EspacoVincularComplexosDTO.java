package br.uece.alunos.sisreserva.v1.dto.espaco;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO para atribuir/desatribuir um espaço a complexos.
 * 
 * @param complexoIds lista de IDs dos complexos
 */
public record EspacoVincularComplexosDTO(
        @NotEmpty(message = "A lista de complexos não pode ser vazia")
        List<String> complexoIds
) {}
