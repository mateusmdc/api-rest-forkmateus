package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.specification.GestorEspacoSpecification;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ObterGestorEspaco {
    private final GestorEspacoRepository repository;

    public Page<GestorEspacoRetornoDTO> obter(Pageable pageable, String id, String espacoId, String gestorId, Boolean todos) {
        Map<String, Object> filtros = new HashMap<>();

        if (id != null) filtros.put("id", id);
        if (espacoId != null) filtros.put("espacoId", espacoId);
        if (gestorId != null) filtros.put("gestorId", gestorId);
        if (Boolean.TRUE.equals(todos)) filtros.put("todos", true);

        var spec = GestorEspacoSpecification.byFilters(filtros);

        return repository.findAll(spec, pageable).map(GestorEspacoRetornoDTO::new);
    }
}
