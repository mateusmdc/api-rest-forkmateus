package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.cargo.Cargo;
import br.uece.alunos.sisreserva.v1.domain.cargo.useCase.ObterEntCargoPorId;
import br.uece.alunos.sisreserva.v1.domain.cargo.useCase.ObterEntListaCargoPorNome;
import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.departamento.useCase.ObterEntDepartamentoPorId;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ObterEntEspacoPorId;
import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterEntInstituicaoPorId;
import br.uece.alunos.sisreserva.v1.domain.instituicao.useCase.ObterEntInstituicoesPorId;
import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;
import br.uece.alunos.sisreserva.v1.domain.localizacao.useCase.ObterEntLocalizacaoPorId;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividade;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.useCase.ObterEntTipoAtividadePorId;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase.ObterEntTipoEspacoPorId;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.useCase.ObterEntUsuarioPorId;
import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import br.uece.alunos.sisreserva.v1.domain.projeto.useCase.ObterEntProjetoPorId;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntityHandlerServiceImpl implements EntityHandlerService {
    // Cargo
    private final ObterEntCargoPorId obterEntCargoPorId;
    private final ObterEntListaCargoPorNome obterEntListaCargoPorNome;
    @Override
    public List<Cargo> obterEntidadesCargoPorNome(List<String> nomes) {
        return obterEntListaCargoPorNome.obterEntidadesCargoPorNome(nomes);
    }
    @Override
    public Cargo obterCargoPorId(String id) {
        return obterEntCargoPorId.obterEntidadeCargoPorId(id);
    }

    // Departamento
    private final ObterEntDepartamentoPorId obterEntDepartamentoPorId;
    @Override
    public Departamento obterDepartamentoPorId(String id) {
        return obterEntDepartamentoPorId.obterEntidadePorId(id);
    }

    // Espaço
    private final ObterEntEspacoPorId obterEntEspacoPorId;
    @Override
    public Espaco obterEspacoPorId(String id) {
        return obterEntEspacoPorId.obterEntidade(id);
    }

    // Instituicao
    private final ObterEntInstituicaoPorId obterEntInstituicaoPorId;
    private final ObterEntInstituicoesPorId obterEntInstituicoesPorId;
    public Instituicao obterInstituicaoPorId(String id) {
        return obterEntInstituicaoPorId.obterEntidadePorId(id);
    }
    public List<Instituicao> obterInstituicoesPorListaDeId(List<String> ids) {
        return obterEntInstituicoesPorId.obterEntidadesPorListaDeId(ids);
    }

    // Localizacao
    private final ObterEntLocalizacaoPorId obterEntLocalizacaoPorId;
    @Override
    public Localizacao obterLocalizacaoPorId(String id) {
        return obterEntLocalizacaoPorId.obterEntidadePorId(id);
    }

    // Tipo Atividade
    private final ObterEntTipoAtividadePorId obterEntTipoAtividadePorId;
    @Override
    public TipoAtividade obterTipoAtividadePorId(String id) {
        return obterEntTipoAtividadePorId.obterEntidadePorId(id);
    }

    // Tipo Espaco
    private final ObterEntTipoEspacoPorId obterEntTipoEspacoPorId;
    @Override
    public TipoEspaco obterTipoEspacoPorId(String id) {
        return obterEntTipoEspacoPorId.obterEntidadePorId(id);
    }

    // Usuario
    private final ObterEntUsuarioPorId obterEntUsuarioPorId;
    @Override
    public Usuario obterUsuarioPorId(String id) {
        return obterEntUsuarioPorId.obterEntidade(id);
    }

    // Projeto
    private final ObterEntProjetoPorId obterEntProjetoPorId;
    @Override
    public Projeto obterProjetoPorId(String id) {
        return obterEntProjetoPorId.obterEntidade(id);
    }
}