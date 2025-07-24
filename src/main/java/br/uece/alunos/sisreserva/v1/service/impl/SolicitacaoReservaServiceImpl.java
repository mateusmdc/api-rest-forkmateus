package br.uece.alunos.sisreserva.v1.service.impl;


import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase.CriarSolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase.ObterSolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.SolicitacaoReservaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class SolicitacaoReservaServiceImpl implements SolicitacaoReservaService {

    private final CriarSolicitacaoReserva criarSolicitacaoReserva;
    private final ObterSolicitacaoReserva obterSolicitacaoReserva;

    @Override
    public SolicitacaoReservaRetornoDTO criarSolicitacaoReserva(SolicitacaoReservaDTO data) {
        return criarSolicitacaoReserva.criarSolicitacaoReserva(data);
    }

    @Override
    public Page<SolicitacaoReservaRetornoDTO> obterSolicitacaoReserva(
        Pageable pageable,
        String id,
        LocalDate dataInicio,
        LocalDate dataFim,
        String espacoId,
        String usuarioSolicitanteId,
        Integer status,
        String projetoId
    ) {
        return obterSolicitacaoReserva.obterSolicitacaoReserva(pageable, id, dataInicio, dataFim, espacoId, usuarioSolicitanteId, status, projetoId);
    }
    
}
