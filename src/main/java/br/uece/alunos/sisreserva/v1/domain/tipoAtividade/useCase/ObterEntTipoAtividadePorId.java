package br.uece.alunos.sisreserva.v1.domain.tipoAtividade.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividade;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividadeRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntTipoAtividadePorId {
    @Autowired
    private TipoAtividadeRepository repository;

    public TipoAtividade obterEntidadePorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrado tipo de atividade com o ID informado."));
    }
}
