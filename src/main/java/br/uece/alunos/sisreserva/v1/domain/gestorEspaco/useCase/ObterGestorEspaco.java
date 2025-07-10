package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.specification.GestorEspacoSpecification;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ObterGestorEspaco {

    @Autowired
    private GestorEspacoRepository repository;

    public Page<GestorEspacoRetornoDTO> obter(Pageable pageable, String id, String espacoId, String gestorId, Boolean todos) {
        Map<String, Object> filtros = new HashMap<>();

        if (id != null) filtros.put("id", id);
        if (espacoId != null) filtros.put("espacoId", espacoId);
        if (gestorId != null) filtros.put("gestorId", gestorId);
        if (Boolean.TRUE.equals(todos)) filtros.put("todos", true); // marca para não filtrar por estaAtivo

        var spec = GestorEspacoSpecification.byFilters(filtros);

        return repository.findAll(spec, pageable).map(GestorEspacoRetornoDTO::new);
    }

    private Page<GestorEspaco> execute(Map<String, Object> filtros, Pageable pageable) {
        return repository.findAll(
                GestorEspacoSpecification.byFilters(filtros),
                pageable
        );
    }
}
