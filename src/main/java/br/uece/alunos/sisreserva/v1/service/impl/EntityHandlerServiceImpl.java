package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterEntInstituicaoPorId;
import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterEntInstituicoesPorId;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.useCase.ObterEntUsuarioPorId;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EntityHandlerServiceImpl implements EntityHandlerService {
    @Autowired
    private ObterEntInstituicaoPorId obterEntInstituicaoPorId;
    @Autowired
    private ObterEntInstituicoesPorId obterEntInstituicoesPorId;

    @Autowired
    private ObterEntUsuarioPorId obterEntUsuarioPorId;

    public Instituicao obterInstituicaoPorId(String id) {
        return obterEntInstituicaoPorId.obterEntidadePorId(id);
    }

    public List<Instituicao> obterInstituicoesPorListaDeId(List<String> ids) {
        return obterEntInstituicoesPorId.obterEntidadesPorListaDeId(ids);
    }

    public Usuario obterUsuarioPorId(String id) {
        return obterEntUsuarioPorId.obterEntidade(id);
    }
}
