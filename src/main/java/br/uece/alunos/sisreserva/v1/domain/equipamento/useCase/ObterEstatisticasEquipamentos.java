package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasGeralEquipamentoDTO;
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
 * Caso de uso para obter estatísticas de uso dos equipamentos.
 * 
 * <p>Calcula e retorna estatísticas detalhadas sobre o uso dos equipamentos em um período,
 * incluindo estatísticas por mês, mês com mais reservas e usuários que mais
 * reservaram.</p>
 * 
 * <p>Utiliza queries agregadas otimizadas para evitar carregar grandes
 * volumes de dados na memória.</p>
 */
@Component
@RequiredArgsConstructor
public class ObterEstatisticasEquipamentos {
    
    private final EquipamentoRepository equipamentoRepository;
    private final SolicitacaoReservaRepository solicitacaoReservaRepository;
    private final EquipamentoEspacoRepository equipamentoEspacoRepository;
    
    /**
     * Obtém estatísticas de uso dos equipamentos em um período.
     * 
     * @param mesInicial mês inicial para filtrar reservas (opcional, padrão = mês atual)
     * @param anoInicial ano inicial para filtrar reservas (opcional, padrão = ano atual)
     * @param mesFinal mês final para filtrar reservas (opcional, padrão = mês atual)
     * @param anoFinal ano final para filtrar reservas (opcional, padrão = ano atual)
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @param tipoEquipamentoId ID do tipo de equipamento para filtrar (opcional)
     * @param multiusuario filtrar por equipamentos multiusuário (opcional)
     * @param espacoId ID do espaço vinculado para filtrar equipamentos (opcional)
     * @return estatísticas agrupadas por equipamento
     * @throws IllegalArgumentException se os parâmetros forem inválidos ou período inicial maior que final
     */
    public EstatisticasGeralEquipamentoDTO obterEstatisticas(
            Integer mesInicial, 
            Integer anoInicial, 
            Integer mesFinal, 
            Integer anoFinal, 
            List<String> equipamentoIds,
            String tipoEquipamentoId,
            Boolean multiusuario,
            String espacoId) {
        
        //Define período padrão como mês atual se não informado
        YearMonth mesAtual = YearMonth.now();
        int mesInicialConsulta = (mesInicial != null) ? mesInicial : mesAtual.getMonthValue();
        int anoInicialConsulta = (anoInicial != null) ? anoInicial : mesAtual.getYear();
        int mesFinalConsulta = (mesFinal != null) ? mesFinal : mesAtual.getMonthValue();
        int anoFinalConsulta = (anoFinal != null) ? anoFinal : mesAtual.getYear();
        
        // Valida parâmetros
        validarParametros(mesInicialConsulta, anoInicialConsulta, mesFinalConsulta, anoFinalConsulta);
        
        // Obtém a lista de equipamentos a serem analisados
        List<Equipamento> equipamentos = obterEquipamentos(equipamentoIds, tipoEquipamentoId, multiusuario, espacoId);
        
        // Calcula estatísticas para cada equipamento
        List<EstatisticasEquipamentoDTO> estatisticasEquipamentos = equipamentos.stream()
            .map(equipamento -> calcularEstatisticasEquipamento(
                equipamento, 
                mesInicialConsulta, 
                anoInicialConsulta, 
                mesFinalConsulta, 
                anoFinalConsulta))
            .collect(Collectors.toList());
        
        return new EstatisticasGeralEquipamentoDTO(estatisticasEquipamentos);
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
     * Obtém a lista de equipamentos a serem analisados.
     * 
     * @param equipamentoIds lista de IDs (opcional)
     * @param tipoEquipamentoId ID do tipo de equipamento para filtrar (opcional)
     * @param multiusuario filtrar por equipamentos multiusuário (opcional)
     * @param espacoId ID do espaço vinculado para filtrar equipamentos (opcional) - através de EquipamentoEspaco
     * @return lista de equipamentos
     */
    private List<Equipamento> obterEquipamentos(List<String> equipamentoIds, String tipoEquipamentoId, Boolean multiusuario, String espacoId) {
        List<Equipamento> equipamentos;
        
        if (equipamentoIds != null && !equipamentoIds.isEmpty()) {
            equipamentos = equipamentoRepository.findAllById(equipamentoIds);
            
            // Verifica se todos os IDs fornecidos foram encontrados
            if (equipamentos.size() != equipamentoIds.size()) {
                Set<String> encontrados = equipamentos.stream()
                    .map(Equipamento::getId)
                    .collect(Collectors.toSet());
                List<String> naoEncontrados = equipamentoIds.stream()
                    .filter(id -> !encontrados.contains(id))
                    .collect(Collectors.toList());
                throw new EntityNotFoundException(
                    "Equipamentos não encontrados: " + String.join(", ", naoEncontrados)
                );
            }
        } else {
            equipamentos = equipamentoRepository.findAll();
        }
        
        // Busca IDs dos equipamentos vinculados ao espaço (se filtro de espaço foi fornecido)
        Set<String> equipamentosDoEspaco = null;
        if (espacoId != null) {
            equipamentosDoEspaco = new HashSet<>(equipamentoEspacoRepository.findEquipamentosIdsByEspacoId(espacoId));
        }
        
        // Aplica filtros adicionais
        final Set<String> equipamentosDoEspacoFinal = equipamentosDoEspaco;
        return equipamentos.stream()
            .filter(equipamento -> tipoEquipamentoId == null || equipamento.getTipoEquipamento().getId().equals(tipoEquipamentoId))
            .filter(equipamento -> multiusuario == null || equipamento.getMultiusuario().equals(multiusuario))
            .filter(equipamento -> espacoId == null || (equipamentosDoEspacoFinal != null && equipamentosDoEspacoFinal.contains(equipamento.getId())))
            .collect(Collectors.toList());
    }
    
    /**
     * Calcula as estatísticas de um equipamento específico usando queries agregadas otimizadas.
     * 
     * @param equipamento equipamento a ser analisado
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return estatísticas do equipamento
     */
    private EstatisticasEquipamentoDTO calcularEstatisticasEquipamento(
            Equipamento equipamento, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal) {
        
        // Calcula estatísticas por mês do período
        List<ReservasMesDTO> estatisticasPorMes = calcularEstatisticasPorMes(
            equipamento.getId(), mesInicial, anoInicial, mesFinal, anoFinal);
        
        // Calcula totais do período
        TotaisPeriodoDTO totaisPeriodo = calcularTotaisPeriodo(estatisticasPorMes);
        
        // Calcula mês com mais reservas (do período ou do ano se período for 1 mês)
        ReservasMesDTO mesComMaisReservas = calcularMesComMaisReservas(
            equipamento.getId(), mesInicial, anoInicial, mesFinal, anoFinal);
        
        // Calcula todos os usuários que reservaram no período (incluindo não aprovadas)
        List<UsuarioEstatisticaDTO> todosUsuarios = calcularTodosUsuarios(
            equipamento.getId(), mesInicial, anoInicial, mesFinal, anoFinal);
        
        // Calcula usuários que mais reservaram no período (top 10 com reservas aprovadas)
        List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram = calcularUsuariosQueMaisReservaram(
            equipamento.getId(), mesInicial, anoInicial, mesFinal, anoFinal);
        
        return new EstatisticasEquipamentoDTO(
            equipamento.getId(),
            equipamento.getTombamento(),
            equipamento.getTipoEquipamento().getNome(),
            equipamento.getDescricao(),
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
     * @param equipamentoId ID do equipamento
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista de estatísticas por mês, ordenada cronologicamente
     */
    private List<ReservasMesDTO> calcularEstatisticasPorMes(
            String equipamentoId, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal) {
        
        List<ReservasPorMesProjection> reservasPorMes = solicitacaoReservaRepository
            .contarReservasPorEquipamentoNoPeriodo(equipamentoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
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
     * @param equipamentoId ID do equipamento
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return estatísticas do mês com mais reservas confirmadas, ou null se período for de 1 mês
     */
    private ReservasMesDTO calcularMesComMaisReservas(
            String equipamentoId, 
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
            .contarMesComMaisReservasPorEquipamentoNoPeriodo(equipamentoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
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
     * Calcula todos os usuários que fizeram solicitações de reserva no período.
     * Inclui usuários com reservas não aprovadas.
     * 
     * @param equipamentoId ID do equipamento
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista completa de usuários ordenada por quantidade de reservas (decrescente)
     */
    private List<UsuarioEstatisticaDTO> calcularTodosUsuarios(
            String equipamentoId, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal) {
        
        List<ReservasPorUsuarioProjection> reservasPorUsuario = solicitacaoReservaRepository
            .contarTodosUsuariosPorEquipamentoNoPeriodo(equipamentoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
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
     * @param equipamentoId ID do equipamento
     * @param mesInicial mês inicial
     * @param anoInicial ano inicial
     * @param mesFinal mês final
     * @param anoFinal ano final
     * @return lista dos top 10 usuários com mais reservas aprovadas
     */
    private List<UsuarioEstatisticaDTO> calcularUsuariosQueMaisReservaram(
            String equipamentoId, 
            int mesInicial, 
            int anoInicial, 
            int mesFinal, 
            int anoFinal) {
        
        List<ReservasPorUsuarioProjection> reservasPorUsuario = solicitacaoReservaRepository
            .contarReservasPorEquipamentoEUsuarioNoPeriodo(equipamentoId, mesInicial, anoInicial, mesFinal, anoFinal);
        
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
