package br.uece.alunos.sisreserva.v1.domain.instituicao.useCase;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.instituicao.InstituicaoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntInstituicaoPorId {
    @Autowired
    private InstituicaoRepository instituicaoRepository;

    public Instituicao obterEntidadePorId(String id) {
        return instituicaoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrada instituição com o ID informado."));
    }
}
