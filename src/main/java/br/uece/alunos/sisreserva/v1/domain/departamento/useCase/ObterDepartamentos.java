package br.uece.alunos.sisreserva.v1.domain.departamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.departamento.DepartamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.departamento.specification.DepartamentoSpecification;
import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ObterDepartamentos {

    @Autowired
    private DepartamentoRepository repository;

    public Page<DepartamentoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = DepartamentoSpecification.byFilters(filtros);
        var results = repository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = normalize(nome);
            results = results.stream()
                    .filter(dep -> normalize(dep.getNome()).contains(nomeBusca))
                    .toList();
        }

        int total = results.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);
        List<DepartamentoRetornoDTO> page = results.subList(start, end)
                .stream()
                .map(DepartamentoRetornoDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(page, pageable, total);
    }

    private String normalize(String value) {
        if (value == null) return "";
        return Normalizer
                .normalize(value.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
