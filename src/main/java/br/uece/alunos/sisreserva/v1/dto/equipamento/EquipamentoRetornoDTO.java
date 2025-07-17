package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;

public record EquipamentoRetornoDTO(
        String id,
        String tombamento,
        String descricao,
        int status,
        String tipoEquipamentoId,
        String tipoEquipamentoNome
) {
    public EquipamentoRetornoDTO(Equipamento e) {
        this(
                e.getId(),
                e.getTombamento(),
                e.getDescricao(),
                e.getStatus().getCodigo(),
                e.getTipoEquipamento().getId(),
                e.getTipoEquipamento().getNome()
        );
    }
}
