package br.uece.alunos.sisreserva.v1.dto.complexoEspacos;

/**
 * DTO para atualização parcial de complexo de espaços.
 * Todos os campos são opcionais.
 * 
 * @param nome nome do complexo de espaços
 * @param descricao descrição do complexo
 * @param site site do complexo
 */
public record ComplexoEspacosAtualizarDTO(
        String nome,
        String descricao,
        String site
) {}
