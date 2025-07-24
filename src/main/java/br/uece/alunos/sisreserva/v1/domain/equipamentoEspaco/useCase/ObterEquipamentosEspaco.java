package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.specification.EquipamentoEspacoSpecification;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.UtilsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@AllArgsConstructor
public class ObterEquipamentosEspaco {

    private final EquipamentoEspacoRepository repository;
    private final UtilsService utilsService;

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
            String tipoEquipamentoNomeBusca = utilsService.normalizeString(tipoEquipamentoNome);
            String espacoNomeBusca = utilsService.normalizeString(espacoNome);

            resultados = resultados.stream().filter(e -> {
                boolean condicaoTipoEquipamento = true;
                boolean condicaoEspaco = true;

                if (!tipoEquipamentoNomeBusca.isBlank()) {
                    condicaoTipoEquipamento = utilsService.normalizeString(e.getEquipamento().getTipoEquipamento().getNome()).contains(tipoEquipamentoNomeBusca);
                }
                if (!espacoNomeBusca.isBlank()) {
                    condicaoEspaco = utilsService.normalizeString(e.getEspaco().getNome()).contains(espacoNomeBusca);
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
                .toList();

        return new PageImpl<>(page, pageable, total);
    }
}
