package br.uece.alunos.sisreserva.v1.dto.comite;

import br.uece.alunos.sisreserva.v1.domain.comite.TipoComite;
import jakarta.validation.constraints.NotNull;

public record ComiteDTO(String descricao,
                        @NotNull TipoComite tipo
                        ) {
}
