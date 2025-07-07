package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.cargo.Cargo;
import br.uece.alunos.sisreserva.v1.domain.cargo.useCase.ObterEntCargoPorId;
import br.uece.alunos.sisreserva.v1.domain.cargo.useCase.ObterEntListaCargoPorNome;
import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.departamento.useCase.ObterEntDepartamentoPorId;
import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterEntInstituicaoPorId;
import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterEntInstituicoesPorId;
import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;
import br.uece.alunos.sisreserva.v1.domain.localizacao.useCase.ObterEntLocalizacaoPorId;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase.ObterEntTipoEspacoPorId;
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
    //Cargo
    @Autowired
    private ObterEntCargoPorId obterEntCargoPorId;
    @Autowired
    private ObterEntListaCargoPorNome obterEntListaCargoPorNome;
    @Override
    public List<Cargo> obterEntidadesCargoPorNome(List<String> nomes) {
        return obterEntListaCargoPorNome.obterEntidadesCargoPorNome(nomes);
    }
    @Override
    public Cargo obterCargoPorId(String id) {
        return obterEntCargoPorId.obterEntidadeCargoPorId(id);
    }

    //Departamento
    @Autowired
    private ObterEntDepartamentoPorId obterEntDepartamentoPorId;
    @Override
    public Departamento obterDepartamentoPorId(String id) {
        return obterEntDepartamentoPorId.obterEntidadePorId(id);
    }

    //Instituicao
    @Autowired
    private ObterEntInstituicaoPorId obterEntInstituicaoPorId;
    @Autowired
    private ObterEntInstituicoesPorId obterEntInstituicoesPorId;
    public Instituicao obterInstituicaoPorId(String id) {
        return obterEntInstituicaoPorId.obterEntidadePorId(id);
    }
    public List<Instituicao> obterInstituicoesPorListaDeId(List<String> ids) {
        return obterEntInstituicoesPorId.obterEntidadesPorListaDeId(ids);
    }

    @Autowired
    private ObterEntLocalizacaoPorId obterEntLocalizacaoPorId;
    @Override
    public Localizacao obterLocalizacaoPorId(String id) {
        return obterEntLocalizacaoPorId.obterEntidadePorId(id);
    }

    @Autowired
    private ObterEntTipoEspacoPorId obterEntTipoEspacoPorId;
    @Override
    public TipoEspaco obterEspacoPorId(String id) {
        return obterEntTipoEspacoPorId.obterEntidadePorId(id);
    }

    //Usuario
    @Autowired
    private ObterEntUsuarioPorId obterEntUsuarioPorId;
    public Usuario obterUsuarioPorId(String id) {
        return obterEntUsuarioPorId.obterEntidade(id);
    }
}
