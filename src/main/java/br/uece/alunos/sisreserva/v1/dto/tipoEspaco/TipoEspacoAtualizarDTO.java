package br.uece.alunos.sisreserva.v1.dto.tipoEspaco;

/**
 * DTO para atualização parcial de tipo de espaço.
 * Todos os campos são opcionais.
 * 
 * @param nome nome do tipo de espaço
 */
public record TipoEspacoAtualizarDTO(
        String nome
) {}
