package br.uece.alunos.sisreserva.v1.domain.localizacao.useCase;

import br.uece.alunos.sisreserva.v1.domain.localizacao.LocalizacaoRepository;
import br.uece.alunos.sisreserva.v1.domain.localizacao.specification.LocalizacaoSpecification;
import br.uece.alunos.sisreserva.v1.dto.localizacao.LocalizacaoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.*;

@Component
public class ObterLocalizacoes {

    @Autowired
    private LocalizacaoRepository repository;

    public Page<LocalizacaoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = LocalizacaoSpecification.byFilters(filtros);
        var results = repository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = normalize(nome);
            results = results.stream()
                    .filter(loc -> normalize(loc.getNome()).contains(nomeBusca))
                    .toList();
        }

        int total = results.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<LocalizacaoRetornoDTO> page = results.subList(start, end)
                .stream()
                .map(LocalizacaoRetornoDTO::new)
                .toList();

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
