package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.validation.EspacoValidator;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrencia;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrenciaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase.RecorrenciaProcessor;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorarioOcupadoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorDiaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SerieHorariosOcupadosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ObterHorariosOcupadosEspaco {

    @Autowired
    private SolicitacaoReservaRepository solicitacaoReservaRepository;

    @Autowired
    private ExcecaoRecorrenciaRepository excecaoRepository;

    @Autowired
    private EspacoValidator espacoValidator;

    public HorariosOcupadosPorMesDTO obterHorariosOcupadosPorEspaco(String espacoId, Integer mes, Integer ano) {
        espacoValidator.validarEspacoId(espacoId);

        YearMonth yearMonth;
        if (mes != null && ano != null) {
            yearMonth = YearMonth.of(ano, mes);
        } else if (mes != null) {
            yearMonth = YearMonth.of(LocalDate.now().getYear(), mes);
        } else if (ano != null) {
            yearMonth = YearMonth.of(ano, LocalDate.now().getMonthValue());
        } else {
            yearMonth = YearMonth.now();
        }

        LocalDateTime inicioMes = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime fimMes = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // 1) Reservas simples/legadas aprovadas no período (isSerie = false)
        List<SolicitacaoReserva> simples = solicitacaoReservaRepository
                .findReservasAprovadasPorPeriodoEEspaco(inicioMes, fimMes, espacoId);

        Map<LocalDate, List<HorarioOcupadoDTO>> porDia = simples.stream()
                .map(this::converterParaHorarioOcupadoDTO)
                .collect(Collectors.groupingBy(h -> h.dataInicio().toLocalDate()));

        List<HorariosOcupadosPorDiaDTO> diasComHorarios = porDia.entrySet().stream()
                .map(e -> new HorariosOcupadosPorDiaDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(HorariosOcupadosPorDiaDTO::data))
                .collect(Collectors.toList());

        // 2) Séries recorrentes aprovadas com ocorrências no mês (isSerie = true)
        List<SolicitacaoReserva> series = solicitacaoReservaRepository
                .findSeriesAprovadasDoEspacoNoPeriodo(espacoId, inicioMes, fimMes);

        List<SerieHorariosOcupadosDTO> seriesRecorrentes = new ArrayList<>();

        if (!series.isEmpty()) {
            List<String> serieIds = series.stream()
                    .map(SolicitacaoReserva::getId)
                    .collect(Collectors.toList());

            List<ExcecaoRecorrencia> todasExcecoes = excecaoRepository.findBySerieIds(serieIds);
            Map<String, Map<LocalDate, ExcecaoRecorrencia>> excecoesPorSerie = todasExcecoes.stream()
                    .collect(Collectors.groupingBy(
                            ExcecaoRecorrencia::getSolicitacaoReservaId,
                            Collectors.toMap(ExcecaoRecorrencia::getDataOcorrencia, e -> e)
                    ));

            for (SolicitacaoReserva serie : series) {
                long duracaoMinutos = RecorrenciaProcessor.calcularDuracaoEmMinutos(
                        serie.getDataInicio(), serie.getDataFim());
                Map<LocalDate, ExcecaoRecorrencia> excecoesDaSerie =
                        excecoesPorSerie.getOrDefault(serie.getId(), Map.of());

                List<HorarioOcupadoDTO> ocorrenciasNoMes = new ArrayList<>();

                for (LocalDateTime dataOcorrencia : RecorrenciaProcessor.gerarDatasDasOcorrencias(
                        serie.getDataInicio(), serie.getDataFimRecorrencia(), serie.getTipoRecorrencia())) {

                    ExcecaoRecorrencia excecao = excecoesDaSerie.get(dataOcorrencia.toLocalDate());
                    StatusSolicitacao statusEfetivo = excecao != null ? excecao.getStatus() : serie.getStatus();
                    if (statusEfetivo != StatusSolicitacao.APROVADO) continue;

                    LocalDateTime inicioEfetivo = excecao != null && excecao.getDataInicioNova() != null
                            ? excecao.getDataInicioNova() : dataOcorrencia;
                    LocalDateTime fimEfetivo = excecao != null && excecao.getDataFimNova() != null
                            ? excecao.getDataFimNova() : inicioEfetivo.plusMinutes(duracaoMinutos);

                    boolean dentroDoMes = !inicioEfetivo.isAfter(fimMes) && !fimEfetivo.isBefore(inicioMes);
                    if (!dentroDoMes) continue;

                    ocorrenciasNoMes.add(new HorarioOcupadoDTO(
                            serie.getEspaco() != null ? serie.getEspaco().getId() : null,
                            serie.getEspaco() != null ? serie.getEspaco().getNome() : null,
                            inicioEfetivo,
                            fimEfetivo,
                            serie.getUsuarioSolicitante().getNome(),
                            serie.getProjeto() != null ? serie.getProjeto().getNome() : null
                    ));
                }

                if (!ocorrenciasNoMes.isEmpty()) {
                    seriesRecorrentes.add(new SerieHorariosOcupadosDTO(
                            serie.getId(),
                            serie.getEspaco() != null ? serie.getEspaco().getId() : null,
                            serie.getEspaco() != null ? serie.getEspaco().getNome() : null,
                            serie.getUsuarioSolicitante().getNome(),
                            serie.getProjeto() != null ? serie.getProjeto().getNome() : null,
                            ocorrenciasNoMes
                    ));
                }
            }
        }

        return new HorariosOcupadosPorMesDTO(
                yearMonth.getMonthValue(),
                yearMonth.getYear(),
                diasComHorarios,
                seriesRecorrentes
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