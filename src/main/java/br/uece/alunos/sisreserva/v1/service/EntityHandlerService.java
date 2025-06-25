package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.domain.cargo.Cargo;
import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;

import java.util.List;

public interface EntityHandlerService {
    Instituicao obterInstituicaoPorId(String id);
    List<Instituicao> obterInstituicoesPorListaDeId(List<String> ids);

    Usuario obterUsuarioPorId(String id);

    List<Cargo> obterEntidadesCargoPorNome(List<String> nomes);
}
