package br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspaco;

import java.time.LocalDateTime;

public record EquipamentoEspacoRetornoDTO(String id, String equipamentoTombamento, String equipamentoDescricao, String tipoEquipamentoId, String tipoEquipamentoNome,
                                          String espacoId, String espacoNome, LocalDateTime dataAlocacao, LocalDateTime dataRemocao) {
    public EquipamentoEspacoRetornoDTO(EquipamentoEspaco equipamentoEspaco) {
        this(equipamentoEspaco.getId(), equipamentoEspaco.getEquipamento().getTombamento(), equipamentoEspaco.getEquipamento().getDescricao(),
                equipamentoEspaco.getEquipamento().getTipoEquipamento().getId(), equipamentoEspaco.getEquipamento().getTipoEquipamento().getNome(),
                equipamentoEspaco.getEspaco().getId(), equipamentoEspaco.getEspaco().getNome(), equipamentoEspaco.getDataAlocacao(), equipamentoEspaco.getDataRemocao());
    }

}
