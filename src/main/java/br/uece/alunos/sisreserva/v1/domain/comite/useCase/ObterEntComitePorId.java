package br.uece.alunos.sisreserva.v1.domain.comite.useCase;

import br.uece.alunos.sisreserva.v1.domain.comite.Comite;
import br.uece.alunos.sisreserva.v1.domain.comite.ComiteRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntComitePorId {
    @Autowired
    private ComiteRepository repository;

    public Comite obterEntidadePorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrado comitê com o ID informado."));
    }
}
