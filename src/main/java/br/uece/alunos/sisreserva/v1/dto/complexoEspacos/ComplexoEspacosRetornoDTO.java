package br.uece.alunos.sisreserva.v1.dto.complexoEspacos;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacos;

/**
 * DTO para retorno de complexo de espaços.
 * 
 * @param id identificador do complexo
 * @param nome nome do complexo
 * @param descricao descrição do complexo
 * @param site site do complexo
 * @param quantidadeEspacos quantidade de espaços vinculados ao complexo
 */
public record ComplexoEspacosRetornoDTO(
        String id,
        String nome,
        String descricao,
        String site,
        Integer quantidadeEspacos
) {
    public ComplexoEspacosRetornoDTO(ComplexoEspacos complexo) {
        this(
                complexo.getId(),
                complexo.getNome(),
                complexo.getDescricao(),
                complexo.getSite(),
                complexo.getEspacos() != null ? complexo.getEspacos().size() : 0
        );
    }
}
