package br.uece.alunos.sisreserva.v1.domain.instituicao.useCase;

import br.uece.alunos.sisreserva.v1.domain.instituicao.InstituicaoRepository;
import br.uece.alunos.sisreserva.v1.domain.instituicao.specification.InstituicaoSpecification;
import br.uece.alunos.sisreserva.v1.dto.instituicao.InstituicaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ObterInstituicoes {
    private final InstituicaoRepository repository;
    private final UtilsService utilsService;

    public Page<InstituicaoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = InstituicaoSpecification.byFilters(filtros);

        if (nome == null || nome.isBlank()) {
            return repository.findAll(spec, pageable)
                    .map(InstituicaoRetornoDTO::new);
        }

        var todasInstituicoes = repository.findAll(spec, Sort.unsorted());
        var nomeBusca = utilsService.normalizeString(nome);

        var filtrados = todasInstituicoes.stream()
                .filter(inst -> utilsService.normalizeString(inst.getNome()).contains(nomeBusca))
                .toList();

        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), filtrados.size());

        var page = filtrados.subList(start, end)
                .stream()
                .map(InstituicaoRetornoDTO::new)
                .toList();

        return new PageImpl<>(page, pageable, filtrados.size());
    }
}
