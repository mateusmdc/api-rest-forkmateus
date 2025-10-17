package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.validation.EspacoValidator;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorarioOcupadoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorDiaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ObterHorariosOcupadosEspaco {

    @Autowired
    private SolicitacaoReservaRepository solicitacaoReservaRepository;

    @Autowired
    private EspacoValidator espacoValidator;

    public HorariosOcupadosPorMesDTO obterHorariosOcupadosPorEspaco(String espacoId, Integer mes, Integer ano) {
        // Validar se o espaço existe
        espacoValidator.validarEspacoId(espacoId);

        // Determinar o mês e ano a serem usados
        YearMonth yearMonth;
        
        if (mes != null && ano != null) {
            // Ambos informados
            yearMonth = YearMonth.of(ano, mes);
        } else if (mes != null && ano == null) {
            // Apenas mês informado - usa o ano atual
            yearMonth = YearMonth.of(LocalDate.now().getYear(), mes);
        } else if (mes == null && ano != null) {
            // Apenas ano informado - usa o mês atual
            yearMonth = YearMonth.of(ano, LocalDate.now().getMonthValue());
        } else {
            // Nenhum informado - usa mês e ano atual
            yearMonth = YearMonth.now();
        }

        // Definir período do mês
        LocalDateTime inicioMes = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime fimMes = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // Buscar reservas aprovadas do período para o espaço específico
        List<SolicitacaoReserva> reservasAprovadas = solicitacaoReservaRepository
            .findReservasAprovadasPorPeriodoEEspaco(inicioMes, fimMes, espacoId);

        // Converter para DTOs
        List<HorarioOcupadoDTO> horariosOcupados = reservasAprovadas.stream()
            .map(this::converterParaHorarioOcupadoDTO)
            .collect(Collectors.toList());

        // Agrupar por dia
        Map<LocalDate, List<HorarioOcupadoDTO>> horariosAgrupadosPorDia = horariosOcupados.stream()
            .collect(Collectors.groupingBy(horario -> horario.dataInicio().toLocalDate()));

        // Converter para lista de dias com horários ocupados
        List<HorariosOcupadosPorDiaDTO> diasComHorarios = horariosAgrupadosPorDia.entrySet().stream()
            .map(entry -> new HorariosOcupadosPorDiaDTO(entry.getKey(), entry.getValue()))
            .sorted((dia1, dia2) -> dia1.data().compareTo(dia2.data()))
            .collect(Collectors.toList());

        return new HorariosOcupadosPorMesDTO(
            yearMonth.getMonthValue(),
            yearMonth.getYear(),
            diasComHorarios
        );
    }

    private HorarioOcupadoDTO converterParaHorarioOcupadoDTO(SolicitacaoReserva reserva) {
        return new HorarioOcupadoDTO(
            reserva.getEspaco().getId(),
            reserva.getEspaco().getNome(),
            reserva.getDataInicio(),
            reserva.getDataFim(),
            reserva.getUsuarioSolicitante().getNome(),
            reserva.getProjeto() != null ? reserva.getProjeto().getNome() : null
        );
    }
}