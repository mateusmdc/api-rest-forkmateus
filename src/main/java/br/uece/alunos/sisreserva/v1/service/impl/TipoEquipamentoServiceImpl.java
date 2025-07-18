package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase.ObterTiposEquipamento;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.TipoEquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TipoEquipamentoServiceImpl implements TipoEquipamentoService {
    @Autowired
    private ObterTiposEquipamento obterTiposEquipamento;

    @Override
    public Page<TipoEquipamentoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterTiposEquipamento.obter(pageable, id, nome);
    }
}
