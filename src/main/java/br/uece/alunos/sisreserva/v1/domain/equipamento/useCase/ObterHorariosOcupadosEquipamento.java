package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.validation.EquipamentoValidator;
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

/**
 * Caso de uso para obter os horários ocupados de um equipamento em um mês específico.
 *
 * Combina dois tipos de fontes:
 * 
 *   - Reservas simples (registros com {@code isSerie = false}) aprovadas no mês.
 *   - Séries recorrentes aprovadas (registros com {@code isSerie = true}) cujas ocorrências
 *       calculadas caem no mês – aplicando exceções quando existirem.
 * 
 *
 * Segue o mesmo padrão de {@code ObterHorariosOcupadosEspaco}, porém filtrando pelo
 * campo {@code equipamento.id} da solicitação de reserva.
 *
 * @author Sistema de Reservas UECE
 * @see br.uece.alunos.sisreserva.v1.domain.espaco.useCase.ObterHorariosOcupadosEspaco
 */
@Component
public class ObterHorariosOcupadosEquipamento {

    @Autowired
    private SolicitacaoReservaRepository solicitacaoReservaRepository;

    @Autowired
    private ExcecaoRecorrenciaRepository excecaoRepository;

    @Autowired
    private EquipamentoValidator equipamentoValidator;

    /**
     * Retorna os horários ocupados de um equipamento em um mês específico.
     *
     * <p>Reservas simples são agrupadas por dia em {@code diasComHorariosOcupados}.
     * Séries recorrentes são listadas separadamente em {@code seriesRecorrentes}, cada uma
     * com suas ocorrências efetivas no mês (após aplicação de eventuais exceções).</p>
     *
     * @param equipamentoId ID do equipamento; deve existir no banco
     * @param mes           mês (1-12); se {@code null} e {@code ano} também for nulo, usa o mês atual;
     *                      se apenas {@code mes} for informado, combina com o ano atual
     * @param ano           ano; se {@code null} e {@code mes} também for nulo, usa o ano atual;
     *                      se apenas {@code ano} for informado, combina com o mês atual
     * @return horários ocupados agrupados por dia e séries recorrentes
     */
    public HorariosOcupadosPorMesDTO obterHorariosOcupadosPorEquipamento(
            String equipamentoId, Integer mes, Integer ano) {

        equipamentoValidator.validarEquipamentoId(equipamentoId);

        // Determinar o mês/ano alvo, aceitando informação parcial
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
        LocalDateTime fimMes    = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        // ─── 1) Reservas simples aprovadas no período (isSerie = false) ───────────────
        List<SolicitacaoReserva> simples = solicitacaoReservaRepository
                .findReservasAprovadasSimplesPorPeriodoEEquipamento(inicioMes, fimMes, equipamentoId);

        // Agrupar reservas simples por dia
        Map<LocalDate, List<HorarioOcupadoDTO>> porDia = simples.stream()
                .map(this::converterParaHorarioOcupadoDTO)
                .collect(Collectors.groupingBy(h -> h.dataInicio().toLocalDate()));

        List<HorariosOcupadosPorDiaDTO> diasComHorarios = porDia.entrySet().stream()
                .map(e -> new HorariosOcupadosPorDiaDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(HorariosOcupadosPorDiaDTO::data))
                .collect(Collectors.toList());

        // ─── 2) Séries recorrentes aprovadas com ocorrências no mês (isSerie = true) ──
        List<SolicitacaoReserva> series = solicitacaoReservaRepository
                .findSeriesAprovadasDoEquipamentoNoPeriodo(equipamentoId, inicioMes, fimMes);

        List<SerieHorariosOcupadosDTO> seriesRecorrentes = new ArrayList<>();

        if (!series.isEmpty()) {
            // Carregar exceções de todas as séries em lote (evitar N+1)
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

                    // Ocorrências canceladas ou recusadas não ocupam horário
                    StatusSolicitacao statusEfetivo = excecao != null ? excecao.getStatus() : serie.getStatus();
                    if (statusEfetivo != StatusSolicitacao.APROVADO) continue;

                    LocalDateTime inicioEfetivo = excecao != null && excecao.getDataInicioNova() != null
                            ? excecao.getDataInicioNova() : dataOcorrencia;
                    LocalDateTime fimEfetivo = excecao != null && excecao.getDataFimNova() != null
                            ? excecao.getDataFimNova() : inicioEfetivo.plusMinutes(duracaoMinutos);

                    // Verificar se esta ocorrência está dentro do mês (usando horários efetivos)
                    boolean dentroDoMes = !inicioEfetivo.isAfter(fimMes) && !fimEfetivo.isBefore(inicioMes);
                    if (!dentroDoMes) continue;

                    ocorrenciasNoMes.add(new HorarioOcupadoDTO(
                            null,
                            null,
                            serie.getEquipamento() != null ? serie.getEquipamento().getId() : null,
                            serie.getEquipamento() != null ? serie.getEquipamento().getTombamento() : null,
                            inicioEfetivo,
                            fimEfetivo,
                            serie.getUsuarioSolicitante().getNome(),
                            serie.getProjeto() != null ? serie.getProjeto().getNome() : null
                    ));
                }

                if (!ocorrenciasNoMes.isEmpty()) {
                    seriesRecorrentes.add(new SerieHorariosOcupadosDTO(
                            serie.getId(),
                            null,
                            null,
                            serie.getEquipamento() != null ? serie.getEquipamento().getId() : null,
                            serie.getEquipamento() != null ? serie.getEquipamento().getTombamento() : null,
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

    /**
     * Converte uma reserva simples de equipamento em DTO de horário ocupado.
     */
    private HorarioOcupadoDTO converterParaHorarioOcupadoDTO(SolicitacaoReserva reserva) {
        return new HorarioOcupadoDTO(
                null,
                null,
                reserva.getEquipamento() != null ? reserva.getEquipamento().getId() : null,
                reserva.getEquipamento() != null ? reserva.getEquipamento().getTombamento() : null,
                reserva.getDataInicio(),
                reserva.getDataFim(),
                reserva.getUsuarioSolicitante().getNome(),
                reserva.getProjeto() != null ? reserva.getProjeto().getNome() : null
        );
    }
}
