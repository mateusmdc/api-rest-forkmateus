package br.uece.alunos.sisreserva.v1.dto.comite;

import br.uece.alunos.sisreserva.v1.domain.comite.TipoComite;

public record ComiteDTO(String descricao,
                        TipoComite tipo
                        ) {
}
