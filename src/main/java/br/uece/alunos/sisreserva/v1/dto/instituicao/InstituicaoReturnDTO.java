package br.uece.alunos.sisreserva.v1.dto.instituicao;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;

public record InstituicaoReturnDTO(String id, String nome, String descricao) {
    public InstituicaoReturnDTO(Instituicao instituicao) {
        this(instituicao.getId(), instituicao.getNome(), instituicao.getDescricao());
    }
}
