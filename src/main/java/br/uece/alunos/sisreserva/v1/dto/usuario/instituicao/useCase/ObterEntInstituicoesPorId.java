package br.uece.alunos.sisreserva.v1.dto.usuario.instituicao.useCase;

import br.uece.alunos.sisreserva.v1.dto.usuario.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.dto.usuario.instituicao.InstituicaoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObterEntInstituicoesPorId {
    @Autowired
    private InstituicaoRepository instituicaoRepository;

    public List<Instituicao> obterEntidadesPorListaDeId(List<String> ids) {
        var listaInstituicoes = instituicaoRepository.findAllById(ids);

        if (listaInstituicoes.isEmpty()) {
            throw new ValidationException("Não foi encontrada nenhuma instituição na lista de IDS passados.");
        }

        return listaInstituicoes;
    }
}
