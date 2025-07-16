package br.uece.alunos.sisreserva.v1.dto.tipoAtividade;

import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividade;

public record TipoAtividadeRetornoDTO(String id, String nome) {
    public TipoAtividadeRetornoDTO(TipoAtividade tipoAtividade) {
        this(tipoAtividade.getId(), tipoAtividade.getNome());
    }
}
