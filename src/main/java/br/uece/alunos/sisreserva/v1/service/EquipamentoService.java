package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipamentoService {
    EquipamentoRetornoDTO atualizar(String id, EquipamentoAtualizarDTO data);
    EquipamentoRetornoDTO criar(EquipamentoDTO data);
    void deletar(String id);
    Page<EquipamentoRetornoDTO> obter(Pageable pageable, String id, String tombamento, String status, String tipoEquipamento, Boolean reservavel);
}
