package br.uece.alunos.sisreserva.v1.dto.complexoEspacos;

import jakarta.validation.constraints.NotEmpty;

/**
 * DTO para criação de complexo de espaços.
 * 
 * @param nome nome do complexo de espaços
 * @param descricao descrição do complexo (opcional)
 * @param site site do complexo (opcional)
 */
public record ComplexoEspacosDTO(
        @NotEmpty(message = "O nome não pode ser vazio")
        String nome,
        String descricao,
        String site
) {}
