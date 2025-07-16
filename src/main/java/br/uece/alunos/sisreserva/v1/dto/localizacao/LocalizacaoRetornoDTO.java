package br.uece.alunos.sisreserva.v1.dto.localizacao;

import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;

public record LocalizacaoRetornoDTO(String id, String nome) {
    public LocalizacaoRetornoDTO(Localizacao localizacao) {
        this(localizacao.getId(), localizacao.getNome());
    }
}
