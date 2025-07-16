package br.uece.alunos.sisreserva.v1.domain.instituicao.useCase;

import br.uece.alunos.sisreserva.v1.domain.instituicao.InstituicaoRepository;
import br.uece.alunos.sisreserva.v1.domain.instituicao.specification.InstituicaoSpecification;
import br.uece.alunos.sisreserva.v1.dto.instituicao.InstituicaoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ObterInstituicoes {
    @Autowired
    private InstituicaoRepository repository;

    public Page<InstituicaoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);

        var spec = InstituicaoSpecification.byFilters(filtros);

        // Se não tiver filtro por nome, faz a paginação direto no banco
        if (nome == null || nome.isBlank()) {
            return repository.findAll(spec, pageable)
                    .map(InstituicaoRetornoDTO::new);
        }

        // Se tiver filtro por nome, busca tudo e filtra em memória, normalizando a string
        var todasInstituicoes = repository.findAll(spec, Sort.unsorted());
        var nomeBusca = normalize(nome);

        var filtrados = todasInstituicoes.stream()
                .filter(inst -> normalize(inst.getNome()).contains(nomeBusca))
                .toList();

        // Pagina manualmente os resultados já filtrados em memória
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), filtrados.size());

        var page = filtrados.subList(start, end)
                .stream()
                .map(InstituicaoRetornoDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(page, pageable, filtrados.size());
    }


    private String normalize(String value) {
        if (value == null) return "";
        return Normalizer
                .normalize(value.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // remove acentos
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
