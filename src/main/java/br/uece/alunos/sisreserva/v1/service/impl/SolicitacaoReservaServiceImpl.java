package br.uece.alunos.sisreserva.v1.service.impl;


import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase.CriarSolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase.ObterSolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase.AtualizarStatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase.ObterHorariosOcupados;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.AtualizarStatusSolicitacaoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
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
    private final AtualizarStatusSolicitacao atualizarStatusSolicitacao;
    private final ObterHorariosOcupados obterHorariosOcupados;

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

    @Override
    public SolicitacaoReservaRetornoDTO atualizarStatus(String id, AtualizarStatusSolicitacaoDTO data) {
        return atualizarStatusSolicitacao.atualizarStatus(id, data);
    }

    @Override
    public HorariosOcupadosPorMesDTO obterHorariosOcupadosPorMes(Integer mes, Integer ano, String espacoId) {
        return obterHorariosOcupados.obterHorariosOcupadosPorMes(mes, ano, espacoId);
    }
}
