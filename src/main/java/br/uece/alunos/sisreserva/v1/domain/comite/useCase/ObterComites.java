package br.uece.alunos.sisreserva.v1.domain.comite.useCase;

import br.uece.alunos.sisreserva.v1.domain.comite.Comite;
import br.uece.alunos.sisreserva.v1.domain.comite.ComiteRepository;
import br.uece.alunos.sisreserva.v1.domain.comite.TipoComite;
import br.uece.alunos.sisreserva.v1.domain.comite.specification.ComiteSpecification;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ObterComites {

    @Autowired
    private ComiteRepository repository;

    public Page<ComiteRetornoDTO> obter(Pageable pageable, String id, Integer tipoCodigo) {
        Map<String, Object> filtros = new HashMap<>();

        if (id != null) filtros.put("id", id);
        if (tipoCodigo != null) filtros.put("tipo", TipoComite.fromCodigo(tipoCodigo));

        var spec = ComiteSpecification.byFilters(filtros);

        return repository.findAll(spec, pageable).map(ComiteRetornoDTO::new);
    }
}
