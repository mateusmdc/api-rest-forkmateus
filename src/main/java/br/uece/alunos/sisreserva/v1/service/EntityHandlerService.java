package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.domain.cargo.Cargo;
import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividade;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;

import java.util.List;

public interface EntityHandlerService {
    //cargo
    List<Cargo> obterEntidadesCargoPorNome(List<String> nomes);
    Cargo obterCargoPorId(String id);

    //departamento
    Departamento obterDepartamentoPorId(String id);

    //instituição
    Instituicao obterInstituicaoPorId(String id);
    List<Instituicao> obterInstituicoesPorListaDeId(List<String> ids);

    //localização
    Localizacao obterLocalizacaoPorId(String id);

    //tipo de atividade
    TipoAtividade obterTipoAtividadePorId(String id);

    //tipo de espaço
    TipoEspaco obterTipoEspacoPorId(String id);

    //usuario
    Usuario obterUsuarioPorId(String id);
}
