package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
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
        
        // Calcula estatísticas por mês do período
        List<ReservasMesDTO> estatisticasPorMes = calcularEstatisticasPorMes(
            espaco.getId(), mesInicial, anoInicial, mesFinal, anoFinal);
        
        // Calcula totais do período
        TotaisPeriodoDTO totaisPeriodo = calcularTotaisPeriodo(estatisticasPorMes);
        
        // Calcula mês com mais reservas (do período ou do ano se período for 1 mês)
        ReservasMesDTO mesComMaisReservas = calcularMesComMaisReservas(
            espaco.getId(), mesInicial, anoInicial, mesFinal, anoFinal);
        
        // Calcula todos os usuários que reservaram no período (incluindo não aprovadas)
        List<UsuarioEstatisticaDTO> todosUsuarios = calcularTodosUsuarios(
            espaco.getId(), mesInicial, anoInicial, mesFinal, anoFinal);
        
        // Calcula usuários que mais reservaram no período (top 10 com reservas aprovadas)
        List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram = calcularUsuariosQueMaisReservaram(
            espaco.getId(), mesInicial, anoInicial, mesFinal, anoFinal);
        
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
            int anoFinal) {
        
        List<ReservasPorMesProjection> reservasPorMes = solicitacaoReservaRepository
            .contarReservasPorEspacoNoPeriodo(espacoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
        if (reservasPorMes.isEmpty()) {
            // Se não houver reservas, retorna lista com zeros para cada mês do período
            return gerarMesesVazios(mesInicial, anoInicial, mesFinal, anoFinal);
        }
        
        // Converte projeções para DTOs, preenchendo meses sem reservas com zeros
        return preencherMesesComZeros(reservasPorMes, mesInicial, anoInicial, mesFinal, anoFinal);
    }
    
    /**
     * Gera lista de meses vazios (com zeros) para o período.
     * 
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista de meses com estatísticas zeradas
     */
    private List<ReservasMesDTO> gerarMesesVazios(int mesInicial, int anoInicial, int mesFinal, int anoFinal) {
        List<ReservasMesDTO> meses = new ArrayList<>();
        YearMonth periodo = YearMonth.of(anoInicial, mesInicial);
        YearMonth periodoFinal = YearMonth.of(anoFinal, mesFinal);
        
        while (!periodo.isAfter(periodoFinal)) {
            meses.add(new ReservasMesDTO(periodo.getMonthValue(), periodo.getYear(), 0L, 0L));
            periodo = periodo.plusMonths(1);
        }
        
        return meses;
    }
    
    /**
     * Preenche meses sem reservas com zeros, mantendo ordem cronológica.
     * 
     * @param reservasPorMes lista de projeções com dados
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista completa de meses, com zeros onde não há dados
     */
    private List<ReservasMesDTO> preencherMesesComZeros(
            List<ReservasPorMesProjection> reservasPorMes,
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal) {
        
        // Converte projeções para mapa para acesso rápido
        Map<String, ReservasPorMesProjection> mapaReservas = reservasPorMes.stream()
            .collect(Collectors.toMap(
                p -> p.getAno() + "-" + p.getMes(),
                p -> p
            ));
        
        // Gera lista completa de meses
        List<ReservasMesDTO> meses = new ArrayList<>();
        YearMonth periodo = YearMonth.of(anoInicial, mesInicial);
        YearMonth periodoFinal = YearMonth.of(anoFinal, mesFinal);
        
        while (!periodo.isAfter(periodoFinal)) {
            String chave = periodo.getYear() + "-" + periodo.getMonthValue();
            ReservasPorMesProjection reservas = mapaReservas.get(chave);
            
            if (reservas != null) {
                meses.add(new ReservasMesDTO(
                    reservas.getMes(),
                    reservas.getAno(),
                    reservas.getTotalReservas(),
                    reservas.getReservasConfirmadas()
                ));
            } else {
                meses.add(new ReservasMesDTO(periodo.getMonthValue(), periodo.getYear(), 0L, 0L));
            }
            
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
            String espacoId, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal) {
        
        // Verifica se o período é de um único mês
        boolean periodoUnicoMes = (mesInicial == mesFinal && anoInicial == anoFinal);
        
        // Se for período de 1 mês, não retorna esta seção
        if (periodoUnicoMes) {
            return null;
        }
        
        // Busca mês com mais reservas confirmadas do período
        List<ReservasPorMesProjection> resultado = solicitacaoReservaRepository
            .contarMesComMaisReservasPorEspacoNoPeriodo(espacoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
        if (resultado.isEmpty()) {
            // Se não houver reservas, retorna null
            return null;
        }
        
        // Retorna o primeiro (que tem mais reservas confirmadas)
        ReservasPorMesProjection mesComMais = resultado.get(0);
        return new ReservasMesDTO(
            mesComMais.getMes(),
            mesComMais.getAno(),
            mesComMais.getTotalReservas(),
            mesComMais.getReservasConfirmadas()
        );
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
            int anoFinal) {
        
        List<ReservasPorUsuarioProjection> reservasPorUsuario = solicitacaoReservaRepository
            .contarTodosUsuariosPorEspacoNoPeriodo(espacoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
        return reservasPorUsuario.stream()
            .map(projection -> new UsuarioEstatisticaDTO(
                projection.getUsuarioId(),
                projection.getUsuarioNome(),
                projection.getTotalReservas(),
                projection.getReservasConfirmadas()
            ))
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
            int anoFinal) {
        
        List<ReservasPorUsuarioProjection> reservasPorUsuario = solicitacaoReservaRepository
            .contarReservasPorEspacoEUsuarioNoPeriodo(espacoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
        return reservasPorUsuario.stream()
            .limit(10)
            .map(projection -> new UsuarioEstatisticaDTO(
                projection.getUsuarioId(),
                projection.getUsuarioNome(),
                projection.getTotalReservas(),
                projection.getReservasConfirmadas()
            ))
            .collect(Collectors.toList());
    }
}
