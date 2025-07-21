package br.uece.alunos.sisreserva.v1.domain.comiteUsuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.ComiteUsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.specification.ComiteUsuarioSpecification;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ObterComiteUsuarios {

    @Autowired
    private ComiteUsuarioRepository repository;

    public Page<ComiteUsuarioRetornoDTO> obter(
            Pageable pageable,
            String id,
            String comiteId,
            String usuarioId,
            String departamentoId,
            String portaria,
            Boolean isTitular
    ) {
        Map<String, Object> filtros = new HashMap<>();

        if (id != null) filtros.put("id", id);
        if (comiteId != null) filtros.put("comiteId", comiteId);
        if (usuarioId != null) filtros.put("usuarioId", usuarioId);
        if (departamentoId != null) filtros.put("departamentoId", departamentoId);
        if (portaria != null) filtros.put("portaria", portaria);
        if (isTitular != null) filtros.put("isTitular", isTitular);

        var spec = ComiteUsuarioSpecification.byFilters(filtros);

        return repository.findAll(spec, pageable).map(ComiteUsuarioRetornoDTO::new);
    }
}
