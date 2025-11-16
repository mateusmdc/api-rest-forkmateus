package br.uece.alunos.sisreserva.v1.dto.complexoEspacos;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO para atribuir/desatribuir espaços a um complexo.
 * 
 * @param espacoIds lista de IDs dos espaços
 */
public record ComplexoEspacosVincularEspacosDTO(
        @NotEmpty(message = "A lista de espaços não pode ser vazia")
        List<String> espacoIds
) {}
