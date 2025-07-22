package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoEquipamentoService {
    TipoEquipamentoRetornoDTO atualizar(TipoEquipamentoAtualizarDTO data, String tipoEquipamentoId);
    TipoEquipamentoRetornoDTO criar(TipoEquipamentoDTO data);
    Page<TipoEquipamentoRetornoDTO> obter(Pageable pageable, String id, String nome);
}
