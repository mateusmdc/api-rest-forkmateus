package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.equipamento.useCase.AtualizarEquipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.useCase.CriarEquipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.useCase.ObterEquipamentos;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EquipamentoServiceImpl implements EquipamentoService {
    private final AtualizarEquipamento atualizarEquipamento;
    private final CriarEquipamento criarEquipamento;
    private final ObterEquipamentos obterEquipamentos;

    @Override
    public EquipamentoRetornoDTO atualizar(String id, EquipamentoAtualizarDTO data) {
        return atualizarEquipamento.atualizar(id, data);
    }

    @Override
    public EquipamentoRetornoDTO criar(EquipamentoDTO data) {
        return criarEquipamento.criar(data);
    }

    @Override
    public Page<EquipamentoRetornoDTO> obter(Pageable pageable, String id, String tombamento, String status, String tipoEquipamento, Boolean reservavel) {
        return obterEquipamentos.obter(pageable, id, tombamento, status, tipoEquipamento, reservavel);
    }
}
