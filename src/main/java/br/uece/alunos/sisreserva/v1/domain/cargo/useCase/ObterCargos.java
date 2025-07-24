package br.uece.alunos.sisreserva.v1.domain.cargo.useCase;

import br.uece.alunos.sisreserva.v1.domain.cargo.CargoRepository;
import br.uece.alunos.sisreserva.v1.domain.cargo.specification.CargoSpecification;
import br.uece.alunos.sisreserva.v1.dto.cargo.CargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ObterCargos {

    private final CargoRepository repository;
    private final UtilsService utilsService;

    public Page<CargoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = CargoSpecification.byFilters(filtros);
        var results = repository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = utilsService.normalizeString(nome);
            results = results.stream()
                    .filter(cargo -> utilsService.normalizeString(cargo.getNome()).contains(nomeBusca))
                    .toList();
        }

        int total = results.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);
        List<CargoRetornoDTO> page = results.subList(start, end)
                .stream()
                .map(CargoRetornoDTO::new)
                .toList();

        return new PageImpl<>(page, pageable, total);
    }
}
