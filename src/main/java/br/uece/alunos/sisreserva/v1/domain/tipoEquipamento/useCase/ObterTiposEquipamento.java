package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.specification.TipoEquipamentoSpecification;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ObterTiposEquipamento {

    @Autowired
    private TipoEquipamentoRepository repository;

    public Page<TipoEquipamentoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = TipoEquipamentoSpecification.byFilters(filtros);
        var results = repository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = normalize(nome);
            results = results.stream()
                    .filter(d -> normalize(d.getNome()).contains(nomeBusca))
                    .toList();
        }

        int total = results.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<TipoEquipamentoRetornoDTO> page = results.subList(start, end)
                .stream()
                .map(TipoEquipamentoRetornoDTO::new)
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
