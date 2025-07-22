package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase.AtualizarTipoEquipamento;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase.CriarTipoEquipamento;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase.ObterTiposEquipamento;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.TipoEquipamentoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoEquipamentoServiceImpl implements TipoEquipamentoService {
    private final AtualizarTipoEquipamento atualizarTipoEquipamento;
    private final CriarTipoEquipamento criarTipoEquipamento;
    private final ObterTiposEquipamento obterTiposEquipamento;

    @Override
    public TipoEquipamentoRetornoDTO atualizar(TipoEquipamentoAtualizarDTO data, String tipoEquipamentoId) {
        return atualizarTipoEquipamento.atualizar(data, tipoEquipamentoId);
    }

    @Override
    public TipoEquipamentoRetornoDTO criar(TipoEquipamentoDTO data) {
        return criarTipoEquipamento.criar(data);
    }

    @Override
    public Page<TipoEquipamentoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterTiposEquipamento.obter(pageable, id, nome);
    }
}
