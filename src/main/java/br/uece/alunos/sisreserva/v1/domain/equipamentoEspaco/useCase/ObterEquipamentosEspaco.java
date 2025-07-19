package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.specification.EquipamentoEspacoSpecification;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ObterEquipamentosEspaco {

    @Autowired
    private EquipamentoEspacoRepository repository;

    public Page<EquipamentoEspacoRetornoDTO> obter(Pageable pageable,
                                                   String id,
                                                   String equipamentoId,
                                                   String tipoEquipamentoId,
                                                   String espacoId,
                                                   LocalDateTime dataInicio,
                                                   LocalDateTime dataFim,
                                                   String tipoEquipamentoNome,
                                                   String espacoNome) {

        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (equipamentoId != null) filtros.put("equipamentoId", equipamentoId);
        if (tipoEquipamentoId != null) filtros.put("tipoEquipamentoId", tipoEquipamentoId);
        if (espacoId != null) filtros.put("espacoId", espacoId);
        if (dataInicio != null) filtros.put("dataInicio", dataInicio);
        if (dataFim != null) filtros.put("dataFim", dataFim);

        var spec = EquipamentoEspacoSpecification.byFilters(filtros);
        var resultados = repository.findAll(spec, Sort.unsorted());

        boolean filtrarPorNome = (tipoEquipamentoNome != null && !tipoEquipamentoNome.isBlank()) ||
                (espacoNome != null && !espacoNome.isBlank());

        if (filtrarPorNome) {
            String tipoEquipamentoNomeBusca = normalize(tipoEquipamentoNome);
            String espacoNomeBusca = normalize(espacoNome);

            resultados = resultados.stream().filter(e -> {
                boolean condicaoTipoEquipamento = true;
                boolean condicaoEspaco = true;

                if (!tipoEquipamentoNomeBusca.isBlank()) {
                    condicaoTipoEquipamento = normalize(e.getEquipamento().getTipoEquipamento().getNome()).contains(tipoEquipamentoNomeBusca);
                }
                if (!espacoNomeBusca.isBlank()) {
                    condicaoEspaco = normalize(e.getEspaco().getNome()).contains(espacoNomeBusca);
                }

                return condicaoTipoEquipamento && condicaoEspaco;
            }).toList();
        }

        int total = resultados.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), total);

        List<EquipamentoEspacoRetornoDTO> page = resultados.subList(start, end)
                .stream()
                .map(EquipamentoEspacoRetornoDTO::new)
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
