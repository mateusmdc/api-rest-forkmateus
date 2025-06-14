package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterInstituicaoPorId;
import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterInstituicoesPorId;
import br.uece.alunos.sisreserva.v1.service.InstituicaoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class InstituicaoServiceImpl implements InstituicaoService {
    @Autowired
    private ObterInstituicaoPorId obterInstituicaoPorId;
    @Autowired
    private ObterInstituicoesPorId obterInstituicoesPorId;

    @Override
    public Instituicao obterEntidadePorId(String id) {
        return obterInstituicaoPorId.obterEntidadePorId(id);
    }

    @Override
    public List<Instituicao> obterEntidadesPorListaDeId(List<String> ids) {
        return obterInstituicoesPorId.obterEntidadesPorListaDeId(ids);
    }
}
