package br.uece.alunos.sisreserva.v1.domain.departamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.departamento.DepartamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.departamento.specification.DepartamentoSpecification;
import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ObterDepartamentos {

    private final DepartamentoRepository repository;
    private final UtilsService utilsService;

    public Page<DepartamentoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = DepartamentoSpecification.byFilters(filtros);
        var results = repository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = utilsService.normalizeString(nome);
            results = results.stream()
                    .filter(dep -> utilsService.normalizeString(dep.getNome()).contains(nomeBusca))
                    .toList();
        }

        int total = results.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<DepartamentoRetornoDTO> page = results.subList(start, end)
                .stream()
                .map(DepartamentoRetornoDTO::new)
                .toList();

        return new PageImpl<>(page, pageable, total);
    }
}
