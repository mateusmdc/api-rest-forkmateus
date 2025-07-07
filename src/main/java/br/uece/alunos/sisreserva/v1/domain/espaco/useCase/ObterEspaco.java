package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.specification.EspacoSpecification;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ObterEspaco {
    @Autowired
    private EspacoRepository espacoRepository;

    public Page<EspacoRetornoDTO> obterEspacos(Pageable pageable,
                                               String id,
                                               String departamento,
                                               String localizacao,
                                               String tipoEspaco,
                                               String tipoAtividade) {

        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (departamento != null) filtros.put("departamentoId", departamento);
        if (localizacao != null) filtros.put("localizacaoId", localizacao);
        if (tipoEspaco != null) filtros.put("tipoEspacoId", tipoEspaco);
        if (tipoAtividade != null) filtros.put("tipoAtividadeId", tipoAtividade);

        return execute(filtros, pageable)
                .map(EspacoRetornoDTO::new);
    }

    private Page<Espaco> execute(Map<String, Object> filtros, Pageable pageable) {
        return espacoRepository.findAll(
                EspacoSpecification.byFilter(
                        (String) filtros.get("id"),
                        (String) filtros.get("departamentoId"),
                        (String) filtros.get("localizacaoId"),
                        (String) filtros.get("tipoEspacoId"),
                        (String) filtros.get("tipoAtividadeId")
                ),
                pageable
        );
    }
}
