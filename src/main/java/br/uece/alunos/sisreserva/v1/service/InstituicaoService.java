package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;

import java.util.List;

public interface InstituicaoService {
    List<Instituicao> obterEntidadesPorListaDeId(List<String> ids);
}
