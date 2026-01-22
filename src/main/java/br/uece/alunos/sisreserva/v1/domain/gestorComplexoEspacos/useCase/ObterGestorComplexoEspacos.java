package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.GestorComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.specification.GestorComplexoEspacosSpecification;
import br.uece.alunos.sisreserva.v1.dto.gestorComplexoEspacos.GestorComplexoEspacosRetornoDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Use case responsável por obter gestores de complexos de espaços com filtros.
 * Suporta filtros por ID, complexoEspacosId, gestorId e opção de incluir todos (ativos e inativos).
 */
@Slf4j
@Component
@AllArgsConstructor
public class ObterGestorComplexoEspacos {
    private final GestorComplexoEspacosRepository repository;
    private final br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos.validation.GestorComplexoEspacosValidator validator;

    public Page<GestorComplexoEspacosRetornoDTO> obter(Pageable pageable, String id, String complexoEspacosId, String gestorId, Boolean todos) {
        log.info("Buscando gestores de complexos - Filtros: id={}, complexoEspacosId={}, gestorId={}, todos={}", 
                id, complexoEspacosId, gestorId, todos);

        validator.validarPermissaoAdmin();

        Map<String, Object> filtros = new HashMap<>();

        if (id != null) filtros.put("id", id);
        if (complexoEspacosId != null) filtros.put("complexoEspacosId", complexoEspacosId);
        if (gestorId != null) filtros.put("gestorId", gestorId);
        if (Boolean.TRUE.equals(todos)) filtros.put("todos", true);

        var spec = GestorComplexoEspacosSpecification.byFilters(filtros);

        return repository.findAll(spec, pageable).map(GestorComplexoEspacosRetornoDTO::new);
    }
}
