package br.uece.alunos.sisreserva.v1.dto.tipoEspaco;

import jakarta.validation.constraints.NotEmpty;

/**
 * DTO para criação de tipo de espaço.
 * 
 * @param nome nome do tipo de espaço
 */
public record TipoEspacoDTO(
        @NotEmpty(message = "O nome não pode ser vazio")
        String nome
) {}
