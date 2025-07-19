package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoEquipamentoService {
    Page<TipoEquipamentoRetornoDTO> obter(Pageable pageable, String id, String nome);
    TipoEquipamentoRetornoDTO criar(TipoEquipamentoDTO data);
}
