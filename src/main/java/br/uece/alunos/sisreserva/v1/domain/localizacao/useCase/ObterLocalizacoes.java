package br.uece.alunos.sisreserva.v1.domain.localizacao.useCase;

import br.uece.alunos.sisreserva.v1.domain.localizacao.LocalizacaoRepository;
import br.uece.alunos.sisreserva.v1.domain.localizacao.specification.LocalizacaoSpecification;
import br.uece.alunos.sisreserva.v1.dto.localizacao.LocalizacaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ObterLocalizacoes {
    private final LocalizacaoRepository repository;
    private final UtilsService utilsService;

    public Page<LocalizacaoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = LocalizacaoSpecification.byFilters(filtros);
        var results = repository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = utilsService.normalizeString(nome);
            results = results.stream()
                    .filter(loc -> utilsService.normalizeString(loc.getNome()).contains(nomeBusca))
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
}
