package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;

public record EquipamentoRetornoDTO(
        String id,
        String tombamento,
        String descricao,
        int status,
        TipoEquipamentoRetornoDTO tipoEquipamento,
        Boolean multiusuario
) {
    public EquipamentoRetornoDTO(Equipamento e) {
        this(
                e.getId(),
                e.getTombamento(),
                e.getDescricao(),
                e.getStatus().getCodigo(),
                new TipoEquipamentoRetornoDTO(e.getTipoEquipamento()),
                e.getMultiusuario()
        );
    }
}
