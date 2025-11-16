package br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspacoRepository;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeletarTipoEspaco {
    private final TipoEspacoRepository repository;
    private final EntityHandlerService entityHandlerService;

    public void deletar(String id) {
        var tipoEspaco = entityHandlerService.obterTipoEspacoPorId(id);
        repository.delete(tipoEspaco);
    }
}
