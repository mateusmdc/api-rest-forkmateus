package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntEspacoPorId {
    @Autowired
    private EspacoRepository repository;

    public Espaco obterEntidade(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrado espaço com o ID informado."));
    }
}
