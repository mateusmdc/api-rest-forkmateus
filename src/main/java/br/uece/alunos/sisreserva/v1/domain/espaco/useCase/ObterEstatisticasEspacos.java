package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrencia;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.ExcecaoRecorrenciaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.useCase.RecorrenciaProcessor;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasGeralDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasMesDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasPorMesProjection;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasPorUsuarioProjection;
import br.uece.alunos.sisreserva.v1.dto.espaco.TotaisPeriodoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.UsuarioEstatisticaDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Caso de uso para obter estatísticas de uso dos espaços.
 * 
 * <p>Calcula e retorna estatísticas detalhadas sobre o uso dos espaços em um período,
 * incluindo estatísticas por mês, mês com mais reservas e usuários que mais
 * reservaram.</p>
 * 
 * <p>Utiliza queries agregadas otimizadas para evitar carregar grandes
 * volumes de dados na memória.</p>
 */
@Component
@RequiredArgsConstructor
public class ObterEstatisticasEspacos {
    
    private final EspacoRepository espacoRepository;
    private final SolicitacaoReservaRepository solicitacaoReservaRepository;
    private final ExcecaoRecorrenciaRepository excecaoRecorrenciaRepository;
    
    /**
     * Obtém estatísticas de uso dos espaços em um período.
     * 
     * @param mesInicial mês inicial para filtrar reservas (opcional, padrão = mês atual)
     * @param anoInicial ano inicial para filtrar reservas (opcional, padrão = ano atual)
     * @param mesFinal mês final para filtrar reservas (opcional, padrão = mês atual)
     * @param anoFinal ano final para filtrar reservas (opcional, padrão = ano atual)
     * @param espacoIds lista de IDs de espaços para filtrar (opcional, padrão = todos os espaços)
     * @param departamentoId ID do departamento para filtrar espaços (opcional)
     * @param localizacaoId ID da localização para filtrar espaços (opcional)
     * @param tipoEspacoId ID do tipo de espaço para filtrar espaços (opcional)
     * @return estatísticas agrupadas por espaço
     * @throws IllegalArgumentException se os parâmetros forem inválidos ou período inicial maior que final
     */
    public EstatisticasGeralDTO obterEstatisticas(
            Integer mesInicial, 
            Integer anoInicial, 
            Integer mesFinal, 
            Integer anoFinal, 
            List<String> espacoIds,
            String departamentoId,
            String localizacaoId,
            String tipoEspacoId) {
        
        // Define período padrão como mês atual se não informado
        YearMonth mesAtual = YearMonth.now();
        int mesInicialConsulta = (mesInicial != null) ? mesInicial : mesAtual.getMonthValue();
        int anoInicialConsulta = (anoInicial != null) ? anoInicial : mesAtual.getYear();
        int mesFinalConsulta = (mesFinal != null) ? mesFinal : mesAtual.getMonthValue();
        int anoFinalConsulta = (anoFinal != null) ? anoFinal : mesAtual.getYear();
        
        // Valida parâmetros
        validarParametros(mesInicialConsulta, anoInicialConsulta, mesFinalConsulta, anoFinalConsulta);
        
        // Obtém a lista de espaços a serem analisados
        List<Espaco> espacos = obterEspacos(espacoIds, departamentoId, localizacaoId, tipoEspacoId);
        
        // Calcula estatísticas para cada espaço
        List<EstatisticasEspacoDTO> estatisticasEspacos = espacos.stream()
            .map(espaco -> calcularEstatisticasEspaco(
                espaco, 
                mesInicialConsulta, 
                anoInicialConsulta, 
                mesFinalConsulta, 
                anoFinalConsulta))
            .collect(Collectors.toList());
        
        return new EstatisticasGeralDTO(estatisticasEspacos);
    }
    
    /**
     * Valida os parâmetros de mês e ano.
     * 
     * @param mesInicial mês inicial (1-12)
     * @param anoInicial ano inicial
     * @param mesFinal mês final (1-12)
     * @param anoFinal ano final
     * @throws IllegalArgumentException se algum parâmetro for inválido
     */
    private void validarParametros(int mesInicial, int anoInicial, int mesFinal, int anoFinal) {
        // Valida meses
        if (mesInicial < 1 || mesInicial > 12) {
            throw new IllegalArgumentException("Mês inicial inválido. Deve estar entre 1 e 12");
        }
        if (mesFinal < 1 || mesFinal > 12) {
            throw new IllegalArgumentException("Mês final inválido. Deve estar entre 1 e 12");
        }
        
        // Valida anos
        int anoAtual = Year.now().getValue();
        int anoMaximo = anoAtual + 50;
        if (anoInicial < 1900 || anoInicial > anoMaximo) {
            throw new IllegalArgumentException("Ano inicial inválido. Deve estar entre 1900 e " + anoMaximo);
        }
        if (anoFinal < 1900 || anoFinal > anoMaximo) {
            throw new IllegalArgumentException("Ano final inválido. Deve estar entre 1900 e " + anoMaximo);
        }
        
        // Valida que período inicial não é maior que final
        YearMonth periodoInicial = YearMonth.of(anoInicial, mesInicial);
        YearMonth periodoFinal = YearMonth.of(anoFinal, mesFinal);
        if (periodoInicial.isAfter(periodoFinal)) {
            throw new IllegalArgumentException(
                "Período inicial (" + mesInicial + "/" + anoInicial + 
                ") não pode ser maior que período final (" + mesFinal + "/" + anoFinal + ")"
            );
        }
    }
    
    /**
     * Obtém a lista de espaços a serem analisados.
     * 
     * @param espacoIds lista de IDs (opcional)
     * @param departamentoId ID do departamento para filtrar (opcional)
     * @param localizacaoId ID da localização para filtrar (opcional)
     * @param tipoEspacoId ID do tipo de espaço para filtrar (opcional)
     * @return lista de espaços
     */
    private List<Espaco> obterEspacos(List<String> espacoIds, String departamentoId, String localizacaoId, String tipoEspacoId) {
        List<Espaco> espacos;
        
        if (espacoIds != null && !espacoIds.isEmpty()) {
            espacos = espacoRepository.findAllById(espacoIds);
            
            // Verifica se todos os IDs fornecidos foram encontrados
            if (espacos.size() != espacoIds.size()) {
                Set<String> encontrados = espacos.stream()
                    .map(Espaco::getId)
                    .collect(Collectors.toSet());
                List<String> naoEncontrados = espacoIds.stream()
                    .filter(id -> !encontrados.contains(id))
                    .collect(Collectors.toList());
                throw new EntityNotFoundException(
                    "Espaços não encontrados: " + String.join(", ", naoEncontrados)
                );
            }
        } else {
            espacos = espacoRepository.findAll();
        }
        
        // Aplica filtros adicionais
        return espacos.stream()
            .filter(espaco -> departamentoId == null || espaco.getDepartamento().getId().equals(departamentoId))
            .filter(espaco -> localizacaoId == null || espaco.getLocalizacao().getId().equals(localizacaoId))
            .filter(espaco -> tipoEspacoId == null || espaco.getTipoEspaco().getId().equals(tipoEspacoId))
            .collect(Collectors.toList());
    }
    
    /**
     * Calcula as estatísticas de um espaço específico usando queries agregadas otimizadas.
     * 
     * @param espaco espaço a ser analisado
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return estatísticas do espaço
     */
    private EstatisticasEspacoDTO calcularEstatisticasEspaco(
            Espaco espaco, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal) {
        
        // Expande séries recorrentes do espaço no período (isSerie = true) – executado uma única vez
        ExpansaoSeriesStats seriesStats = expandirSeriesNoPeriodo(
            espaco.getId(), true, mesInicial, anoInicial, mesFinal, anoFinal);
        
        // Calcula estatísticas por mês do período (SQL simples + séries expandidas)
        List<ReservasMesDTO> estatisticasPorMes = calcularEstatisticasPorMes(
            espaco.getId(), mesInicial, anoInicial, mesFinal, anoFinal, seriesStats);
        
        // Calcula totais do período
        TotaisPeriodoDTO totaisPeriodo = calcularTotaisPeriodo(estatisticasPorMes);
        
        // Calcula mês com mais reservas a partir das estatísticas já mescladas
        boolean periodoUnicoMes = (mesInicial == mesFinal && anoInicial == anoFinal);
        ReservasMesDTO mesComMaisReservas = calcularMesComMaisReservas(estatisticasPorMes, periodoUnicoMes);
        
        // Calcula todos os usuários que reservaram no período (incluindo não aprovadas)
        List<UsuarioEstatisticaDTO> todosUsuarios = calcularTodosUsuarios(
            espaco.getId(), mesInicial, anoInicial, mesFinal, anoFinal, seriesStats);
        
        // Calcula usuários que mais reservaram no período (top 10 com reservas aprovadas)
        List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram = calcularUsuariosQueMaisReservaram(
            espaco.getId(), mesInicial, anoInicial, mesFinal, anoFinal, seriesStats);
        
        return new EstatisticasEspacoDTO(
            espaco.getId(),
            espaco.getNome(),
            estatisticasPorMes,
            mesComMaisReservas,
            usuariosQueMaisReservaram,
            todosUsuarios,
            totaisPeriodo
        );
    }
    
    /**
     * Calcula estatísticas de reservas para cada mês do período.
     * 
     * @param espacoId ID do espaço
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista de estatísticas por mês, ordenada cronologicamente
     */
    private List<ReservasMesDTO> calcularEstatisticasPorMes(
            String espacoId, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal,
            ExpansaoSeriesStats seriesStats) {
        
        List<ReservasPorMesProjection> reservasPorMes = solicitacaoReservaRepository
            .contarReservasPorEspacoNoPeriodo(espacoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
        Map<String, long[]> sqlMap = reservasPorMes.stream()
            .collect(Collectors.toMap(
                p -> p.getAno() + "-" + p.getMes(),
                p -> new long[]{p.getTotalReservas(), p.getReservasConfirmadas()}
            ));
        
        List<ReservasMesDTO> meses = new ArrayList<>();
        YearMonth periodo = YearMonth.of(anoInicial, mesInicial);
        YearMonth periodoFinal = YearMonth.of(anoFinal, mesFinal);
        
        while (!periodo.isAfter(periodoFinal)) {
            String chave = periodo.getYear() + "-" + periodo.getMonthValue();
            long[] sql = sqlMap.getOrDefault(chave, new long[]{0, 0});
            long[] series = seriesStats.statsPorMes().getOrDefault(periodo, new long[]{0, 0});
            meses.add(new ReservasMesDTO(
                periodo.getMonthValue(), periodo.getYear(),
                sql[0] + series[0], sql[1] + series[1]
            ));
            periodo = periodo.plusMonths(1);
        }
        
        return meses;
    }
    
    /**
     * Calcula totais do período.
     * 
     * @param estatisticasPorMes estatísticas por mês
     * @return totais agregados
     */
    private TotaisPeriodoDTO calcularTotaisPeriodo(List<ReservasMesDTO> estatisticasPorMes) {
        long totalSolicitadas = estatisticasPorMes.stream()
            .mapToLong(ReservasMesDTO::reservasSolicitadas)
            .sum();
        long totalAprovadas = estatisticasPorMes.stream()
            .mapToLong(ReservasMesDTO::reservasConfirmadas)
            .sum();
        
        return new TotaisPeriodoDTO(totalSolicitadas, totalAprovadas);
    }
    
    /**
     * Calcula o mês com mais solicitações de reservas confirmadas no período.
     * Retorna null se o período for de apenas um mês.
     * 
     * @param espacoId ID do espaço
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return estatísticas do mês com mais reservas confirmadas, ou null se período for de 1 mês
     */
    private ReservasMesDTO calcularMesComMaisReservas(
            List<ReservasMesDTO> estatisticasPorMes,
            boolean periodoUnicoMes) {
        
        if (periodoUnicoMes) {
            return null;
        }
        
        return estatisticasPorMes.stream()
            .filter(m -> m.reservasConfirmadas() > 0)
            .max(Comparator.comparingLong(ReservasMesDTO::reservasConfirmadas))
            .orElse(null);
    }
    
    /**
     * Calcula os usuários que mais reservaram o espaço no período.
     * 
     * @param espacoId ID do espaço
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista de usuários ordenada por quantidade de reservas (decrescente)
     */
    /**
     * Calcula todos os usuários que fizeram solicitações de reserva no período.
     * Inclui usuários com reservas não aprovadas.
     * 
     * @param espacoId ID do espaço
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista completa de usuários ordenada por quantidade de reservas (decrescente)
     */
    private List<UsuarioEstatisticaDTO> calcularTodosUsuarios(
            String espacoId, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal,
            ExpansaoSeriesStats seriesStats) {
        
        List<ReservasPorUsuarioProjection> reservasPorUsuario = solicitacaoReservaRepository
            .contarTodosUsuariosPorEspacoNoPeriodo(espacoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
        Map<String, long[]> userMap = new LinkedHashMap<>();
        Map<String, String> nomeMap = new HashMap<>();
        for (ReservasPorUsuarioProjection p : reservasPorUsuario) {
            userMap.put(p.getUsuarioId(), new long[]{p.getTotalReservas(), p.getReservasConfirmadas()});
            nomeMap.put(p.getUsuarioId(), p.getUsuarioNome());
        }
        for (Map.Entry<String, long[]> entry : seriesStats.statsPorUsuario().entrySet()) {
            String userId = entry.getKey();
            long[] sc = entry.getValue();
            nomeMap.putIfAbsent(userId, seriesStats.nomePorUsuario().get(userId));
            long[] ex = userMap.getOrDefault(userId, new long[]{0, 0});
            userMap.put(userId, new long[]{ex[0] + sc[0], ex[1] + sc[1]});
        }
        return userMap.entrySet().stream()
            .map(e -> new UsuarioEstatisticaDTO(e.getKey(), nomeMap.get(e.getKey()), e.getValue()[0], e.getValue()[1]))
            .sorted(Comparator.comparingLong(UsuarioEstatisticaDTO::reservasSolicitadas).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Calcula os top 10 usuários que mais tiveram reservas aprovadas no período.
     * Retorna apenas usuários com pelo menos 1 reserva aprovada.
     * 
     * @param espacoId ID do espaço
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista dos top 10 usuários com mais reservas aprovadas
     */
    private List<UsuarioEstatisticaDTO> calcularUsuariosQueMaisReservaram(
            String espacoId, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal,
            ExpansaoSeriesStats seriesStats) {
        
        List<ReservasPorUsuarioProjection> reservasPorUsuario = solicitacaoReservaRepository
            .contarReservasPorEspacoEUsuarioNoPeriodo(espacoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
        Map<String, long[]> userMap = new LinkedHashMap<>();
        Map<String, String> nomeMap = new HashMap<>();
        for (ReservasPorUsuarioProjection p : reservasPorUsuario) {
            userMap.put(p.getUsuarioId(), new long[]{p.getTotalReservas(), p.getReservasConfirmadas()});
            nomeMap.put(p.getUsuarioId(), p.getUsuarioNome());
        }
        for (Map.Entry<String, long[]> entry : seriesStats.statsPorUsuario().entrySet()) {
            String userId = entry.getKey();
            long[] sc = entry.getValue();
            if (sc[1] == 0) continue;
            nomeMap.putIfAbsent(userId, seriesStats.nomePorUsuario().get(userId));
            long[] ex = userMap.getOrDefault(userId, new long[]{0, 0});
            userMap.put(userId, new long[]{ex[0] + sc[0], ex[1] + sc[1]});
        }
        return userMap.entrySet().stream()
            .filter(e -> e.getValue()[1] > 0)
            .map(e -> new UsuarioEstatisticaDTO(e.getKey(), nomeMap.get(e.getKey()), e.getValue()[0], e.getValue()[1]))
            .sorted(Comparator.comparingLong(UsuarioEstatisticaDTO::reservasConfirmadas).reversed())
            .limit(10)
            .collect(Collectors.toList());
    }

    // ==================== SUPORTE A SÉRIES RECORRENTES (isSerie = true) ====================

    /** Carrega contadores de séries recorrentes expandidas no período para mesclagem com SQL. */
    private record ExpansaoSeriesStats(
        Map<YearMonth, long[]> statsPorMes,
        Map<String, long[]> statsPorUsuario,
        Map<String, String> nomePorUsuario
    ) {}

    private ExpansaoSeriesStats expandirSeriesNoPeriodo(
            String resourceId, boolean isEspaco,
            int mesInicial, int anoInicial, int mesFinal, int anoFinal) {

        YearMonth monthStart = YearMonth.of(anoInicial, mesInicial);
        YearMonth monthEnd   = YearMonth.of(anoFinal, mesFinal);
        LocalDateTime periodoStart = monthStart.atDay(1).atStartOfDay();
        LocalDateTime periodoEnd   = monthEnd.atEndOfMonth().atTime(23, 59, 59);

        List<SolicitacaoReserva> series = isEspaco
            ? solicitacaoReservaRepository.findSeriesDoEspacoNoPeriodo(resourceId, periodoStart, periodoEnd)
            : solicitacaoReservaRepository.findSeriesDoEquipamentoNoPeriodo(resourceId, periodoStart, periodoEnd);

        if (series.isEmpty()) {
            return new ExpansaoSeriesStats(Map.of(), Map.of(), Map.of());
        }

        List<String> serieIds = series.stream().map(SolicitacaoReserva::getId).collect(Collectors.toList());
        List<ExcecaoRecorrencia> allExcecoes = excecaoRecorrenciaRepository.findBySerieIds(serieIds);
        Map<String, Map<LocalDate, ExcecaoRecorrencia>> excecoesPorSerie = allExcecoes.stream()
            .collect(Collectors.groupingBy(
                ExcecaoRecorrencia::getSolicitacaoReservaId,
                Collectors.toMap(ExcecaoRecorrencia::getDataOcorrencia, e -> e)
            ));

        Map<YearMonth, long[]> statsPorMes    = new HashMap<>();
        Map<String, long[]>   statsPorUsuario = new HashMap<>();
        Map<String, String>   nomePorUsuario  = new HashMap<>();

        for (SolicitacaoReserva serie : series) {
            Map<LocalDate, ExcecaoRecorrencia> excDaSerie =
                excecoesPorSerie.getOrDefault(serie.getId(), Map.of());
            String usuarioId   = serie.getUsuarioSolicitante().getId();
            String usuarioNome = serie.getUsuarioSolicitante().getNome();
            nomePorUsuario.put(usuarioId, usuarioNome);

            List<LocalDateTime> ocorrencias = RecorrenciaProcessor.gerarDatasDasOcorrencias(
                serie.getDataInicio(), serie.getDataFimRecorrencia(), serie.getTipoRecorrencia());

            for (LocalDateTime dataOcorrencia : ocorrencias) {
                YearMonth mesOcorrencia = YearMonth.of(
                    dataOcorrencia.getYear(), dataOcorrencia.getMonthValue());
                if (mesOcorrencia.isBefore(monthStart) || mesOcorrencia.isAfter(monthEnd)) continue;

                ExcecaoRecorrencia excecao = excDaSerie.get(dataOcorrencia.toLocalDate());
                StatusSolicitacao statusEfetivo = excecao != null ? excecao.getStatus() : serie.getStatus();

                long[] ms = statsPorMes.computeIfAbsent(mesOcorrencia, k -> new long[]{0, 0});
                ms[0]++;
                if (statusEfetivo == StatusSolicitacao.APROVADO) ms[1]++;

                long[] us = statsPorUsuario.computeIfAbsent(usuarioId, k -> new long[]{0, 0});
                us[0]++;
                if (statusEfetivo == StatusSolicitacao.APROVADO) us[1]++;
            }
        }

        return new ExpansaoSeriesStats(statsPorMes, statsPorUsuario, nomePorUsuario);
    }
}
