package br.uece.alunos.sisreserva.v1.dto.comite;

import br.uece.alunos.sisreserva.v1.domain.comite.Comite;
import br.uece.alunos.sisreserva.v1.domain.comite.TipoComite;

public record ComiteRetornoDTO(String id, String descricao, TipoComite tipo, Integer codigoTipo) {
    public ComiteRetornoDTO(Comite comite) {
        this(comite.getId(), comite.getDescricao(), comite.getTipo(), comite.getTipo().getCodigo());
    }
}
