package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipamentoService {
    EquipamentoRetornoDTO criar(EquipamentoDTO data);
    Page<EquipamentoRetornoDTO> obter(Pageable pageable, String id, String tombamento, String status, String tipoEquipamento);
}
