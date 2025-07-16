package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterInstituicoes;
import br.uece.alunos.sisreserva.v1.dto.instituicao.InstituicaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.InstituicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InstituicaoServiceImpl implements InstituicaoService {
    @Autowired
    private ObterInstituicoes obterInstituicoes;

    @Override
    public Page<InstituicaoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterInstituicoes.obter(pageable, id, nome);
    }
}
