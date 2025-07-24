package br.uece.alunos.sisreserva.v1.domain.tipoAtividade.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividadeRepository;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.specification.TipoAtividadeSpecification;
import br.uece.alunos.sisreserva.v1.dto.tipoAtividade.TipoAtividadeRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ObterTiposAtividade {

    private final TipoAtividadeRepository repository;
    private final UtilsService utilsService;

    public Page<TipoAtividadeRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = TipoAtividadeSpecification.byFilters(filtros);
        var results = repository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = nome != null && !nome.isBlank();
        if (filtrarPorNome) {
            String nomeBusca = utilsService.normalizeString(nome);
            results = results.stream()
                    .filter(d -> utilsService.normalizeString(d.getNome()).contains(nomeBusca))
                    .toList();
        }

        int total = results.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<TipoAtividadeRetornoDTO> page = results.subList(start, end)
                .stream()
                .map(TipoAtividadeRetornoDTO::new)
                .toList();

        return new PageImpl<>(page, pageable, total);
    }
}
