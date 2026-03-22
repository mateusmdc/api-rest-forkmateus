package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrencia;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrenciaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorarioOcupadoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorDiaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Caso de uso para obter horários ocupados em um mês para um espaço.
 *
 * Combina dois tipos de fontes:
 * 
 *   - Reservas simples e legadas (registros com {@code isSerie = false}) aprovadas no mês.
 *   - Séries recorrentes aprovadas (registros com {@code isSerie = true}) cujas ocorrências
 *       calculadas caem no mês – aplicando exceções quando existirem.
 *
 * @author Sistema de Reservas UECE
 * @version 2.0
 */
@Component
public class ObterHorariosOcupados {

    @Autowired
    private SolicitacaoReservaRepository repository;

    @Autowired
    private ExcecaoRecorrenciaRepository excecaoRepository;

    /**
     * Obtém os horários ocupados de um mês específico, opcionalmente filtrados por espaço.
     *
     * @param mes      mês (1-12); se {@code null}, usa o mês atual
     * @param ano      ano; se {@code null}, usa o ano atual
     * @param espacoId ID do espaço para filtrar; se {@code null}, retorna todos os espaços
     * @return horários ocupados agrupados por dia
     */
    public HorariosOcupadosPorMesDTO obterHorariosOcupadosPorMes(Integer mes, Integer ano, String espacoId) {
        YearMonth yearMonth = (mes != null && ano != null)
                ? YearMonth.of(ano, mes)
                : YearMonth.now();

        LocalDateTime inicioMes = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime fimMes    = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<HorarioOcupadoDTO> horariosOcupados = new ArrayList<>();

        // 1) Reservas simples/legadas aprovadas no período (isSerie = false)
        List<SolicitacaoReserva> simples;
        if (espacoId != null && !espacoId.isBlank()) {
            simples = repository.findReservasAprovadasPorPeriodoEEspaco(inicioMes, fimMes, espacoId.trim());
        } else {
            simples = repository.findReservasAprovadasPorPeriodo(inicioMes, fimMes);
        }
        simples.stream()
                .map(this::converterParaHorarioOcupadoDTO)
                .forEach(horariosOcupados::add);

        // 2) Séries recorrentes aprovadas com ocorrências no mês (isSerie = true)
        List<SolicitacaoReserva> series;
        if (espacoId != null && !espacoId.isBlank()) {
            series = repository.findSeriesAprovadasDoEspacoNoPeriodo(espacoId.trim(), inicioMes, fimMes);
        } else {
            series = repository.findSeriesAprovadasNoPeriodo(inicioMes, fimMes);
        }

        if (!series.isEmpty()) {
            // Carregar exceções de todas as séries em lote (evitar N+1)
            List<String> serieIds = series.stream().map(SolicitacaoReserva::getId).collect(Collectors.toList());
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

                // Calcular todas as ocorrências e filtrar as que caem no mês
                List<LocalDateTime> ocorrencias = RecorrenciaProcessor.gerarDatasDasOcorrencias(
                        serie.getDataInicio(),
                        serie.getDataFimRecorrencia(),
                        serie.getTipoRecorrencia()
                );

                for (LocalDateTime dataOcorrencia : ocorrencias) {
                    LocalDateTime dataFimOcorrencia = dataOcorrencia.plusMinutes(duracaoMinutos);

                    // Verificar se esta ocorrência está dentro do mês
                    boolean dentroDoMes = !dataOcorrencia.isAfter(fimMes)
                            && !dataFimOcorrencia.isBefore(inicioMes);
                    if (!dentroDoMes) continue;

                    // Aplicar exceção (se existir) para obter status e horários efetivos
                    ExcecaoRecorrencia excecao = excecoesDaSerie.get(dataOcorrencia.toLocalDate());

                    // Ocorrências canceladas ou recusadas não ocupam horário
                    StatusSolicitacao statusEfetivo = excecao != null ? excecao.getStatus() : serie.getStatus();
                    if (statusEfetivo != StatusSolicitacao.APROVADO) continue;

                    LocalDateTime inicioEfetivo = excecao != null && excecao.getDataInicioNova() != null
                            ? excecao.getDataInicioNova() : dataOcorrencia;
                    LocalDateTime fimEfetivo = excecao != null && excecao.getDataFimNova() != null
                            ? excecao.getDataFimNova() : dataFimOcorrencia;

                    horariosOcupados.add(new HorarioOcupadoDTO(
                            serie.getEspaco() != null ? serie.getEspaco().getId() : null,
                            serie.getEspaco() != null ? serie.getEspaco().getNome() : null,
                            inicioEfetivo,
                            fimEfetivo,
                            serie.getUsuarioSolicitante().getNome(),
                            serie.getProjeto() != null ? serie.getProjeto().getNome() : null
                    ));
                }
            }
        }

        // Agrupar por dia e ordenar
        Map<LocalDate, List<HorarioOcupadoDTO>> porDia = horariosOcupados.stream()
                .collect(Collectors.groupingBy(h -> h.dataInicio().toLocalDate()));

        List<HorariosOcupadosPorDiaDTO> diasComHorarios = porDia.entrySet().stream()
                .map(entry -> new HorariosOcupadosPorDiaDTO(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> a.data().compareTo(b.data()))
                .collect(Collectors.toList());

        return new HorariosOcupadosPorMesDTO(
                yearMonth.getMonthValue(),
                yearMonth.getYear(),
                diasComHorarios
        );
    }

    /**
     * Converte uma reserva simples/legada em DTO de horário ocupado.
     */
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