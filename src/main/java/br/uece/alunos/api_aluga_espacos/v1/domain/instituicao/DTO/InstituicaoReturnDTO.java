package br.uece.alunos.api_aluga_espacos.v1.domain.instituicao.DTO;

import br.uece.alunos.api_aluga_espacos.v1.domain.instituicao.Instituicao;

public record InstituicaoReturnDTO(String id, String nome, String descricao) {
    public InstituicaoReturnDTO(Instituicao instituicao) {
        this(instituicao.getId(), instituicao.getNome(), instituicao.getDescricao());
    }
}
