package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.AtualizarStatusSolicitacaoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SolicitacaoReservaService {
    SolicitacaoReservaRetornoDTO criarSolicitacaoReserva(SolicitacaoReservaDTO data);
    Page<SolicitacaoReservaRetornoDTO> obterSolicitacaoReserva(
        Pageable pageable,
        String id,
        LocalDate dataInicio,
        LocalDate dataFim,
        String espacoId,
        String usuarioSolicitanteId,
        Integer status,
        String projetoId
    );
    
    // Novo método para atualizar status
    SolicitacaoReservaRetornoDTO atualizarStatus(String id, AtualizarStatusSolicitacaoDTO data);
    
    // Novo método para obter horários ocupados
    HorariosOcupadosPorMesDTO obterHorariosOcupadosPorMes(Integer mes, Integer ano);
}
