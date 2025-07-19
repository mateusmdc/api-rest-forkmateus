package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamento.specification.EquipamentoSpecification;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ObterEquipamentos {

    @Autowired
    private EquipamentoRepository repository;

    public Page<EquipamentoRetornoDTO> obter(Pageable pageable, String id, String tombamento, String status, String tipoEquipamento) {
        Map<String, Object> filtros = new HashMap<>();

        if (id != null) filtros.put("id", id);
        if (tombamento != null) filtros.put("tombamento", tombamento);
        if (status != null) filtros.put("status", status);
        if (tipoEquipamento != null) filtros.put("tipoEquipamento", tipoEquipamento);

        var spec = EquipamentoSpecification.byFilters(filtros);

        return repository.findAll(spec, pageable).map(EquipamentoRetornoDTO::new);
    }

    private Page<Equipamento> execute(Map<String, Object> filtros, Pageable pageable) {
        return repository.findAll(
                EquipamentoSpecification.byFilters(filtros),
                pageable
        );
    }
}
