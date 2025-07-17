package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.equipamento.useCase.ObterEquipamentos;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EquipamentoServiceImpl implements EquipamentoService {
    @Autowired
    private ObterEquipamentos obterEquipamentos;

    @Override
    public Page<EquipamentoRetornoDTO> obter(Pageable pageable, String id, String tombamento, String status, String tipoEquipamento) {
        return obterEquipamentos.obter(pageable, id, tombamento, status, tipoEquipamento);
    }
}
