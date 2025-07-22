package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.domain.equipamento.StatusEquipamento;

public record EquipamentoAtualizarDTO(String usuarioId, String descricao, StatusEquipamento status) {
}
