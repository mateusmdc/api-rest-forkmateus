package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.specification.SolicitacaoReservaSpecification;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class ObterSolicitacaoReserva {
    @Autowired
    private SolicitacaoReservaRepository solicitacaoReservaRepository;

    public Page<SolicitacaoReservaRetornoDTO> obterSolicitacaoReserva(
        Pageable pageable,
        String id,
        LocalDate dataInicio,
        LocalDate dataFim,
        String espacoId,
        String equipamentoId,
        String usuarioSolicitanteId,
        Integer statusCodigo,
        String projetoId
    ) {
        Map<String, Object> filtros = new HashMap<>();
        if (id != null) filtros.put("id", id);
        if (dataInicio != null) filtros.put("dataInicio", dataInicio);
        if (dataFim != null) filtros.put("dataFim", dataFim);
        if (espacoId != null) filtros.put("espacoId", espacoId);
        if (equipamentoId != null) filtros.put("equipamentoId", equipamentoId);
        if (usuarioSolicitanteId != null) filtros.put("usuarioSolicitanteId", usuarioSolicitanteId);
        if (statusCodigo != null) filtros.put("statusCodigo", statusCodigo);
        if (projetoId != null) filtros.put("projetoId", projetoId);

        return execute(filtros, pageable).map(SolicitacaoReservaRetornoDTO::new);
    }

    private Page<SolicitacaoReserva> execute(Map<String, Object> filtros, Pageable pageable) {
        return solicitacaoReservaRepository.findAll(
            SolicitacaoReservaSpecification.byFilter(
                (String) filtros.get("id"),
                (LocalDate) filtros.get("dataInicio"),
                (LocalDate) filtros.get("dataFim"),
                (String) filtros.get("espacoId"),
                (String) filtros.get("equipamentoId"),
                (String) filtros.get("usuarioSolicitanteId"),
                (Integer) filtros.get("statusCodigo"),
                (String) filtros.get("projetoId")
            ),
            pageable
        );
    }
}
