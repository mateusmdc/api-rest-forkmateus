package br.uece.alunos.sisreserva.v1.dto.tipoEspaco;

import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;

public record TipoEspacoRetornoDTO(String id, String nome) {
    public TipoEspacoRetornoDTO(TipoEspaco tipoEspaco) {
        this(tipoEspaco.getId(), tipoEspaco.getNome());
    }
}