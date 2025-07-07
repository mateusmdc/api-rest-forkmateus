package br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntTipoEspacoPorId {
    @Autowired
    private TipoEspacoRepository repository;

    public TipoEspaco obterEntidadePorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrado tipo de espaço com o ID informado."));
    }
}
