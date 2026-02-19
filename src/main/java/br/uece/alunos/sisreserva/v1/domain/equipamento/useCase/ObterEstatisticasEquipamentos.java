package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasGeralEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasMesDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasPorMesProjection;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasPorUsuarioProjection;
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
 * <p>Calcula e retorna estatísticas detalhadas sobre o uso dos equipamentos,
 * incluindo reservas do mês, mês com mais reservas e usuários que mais
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
    
    /**
     * Obtém estatísticas de uso dos equipamentos.
     * 
     * @param mes mês para filtrar reservas (opcional, padrão = mês atual)
     * @param ano ano para filtrar reservas (opcional, padrão = ano atual)
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @return estatísticas agrupadas por equipamento
     * @throws IllegalArgumentException se o mês ou ano fornecido for inválido
     */
    public EstatisticasGeralEquipamentoDTO obterEstatisticas(Integer mes, Integer ano, List<String> equipamentoIds) {
        // Valida mês se informado
        if (mes != null && (mes < 1 || mes > 12)) {
            throw new IllegalArgumentException("Mês inválido. Deve estar entre 1 e 12");
        }
        
        // Valida ano se informado
        if (ano != null) {
            int anoAtual = Year.now().getValue();
            int anoMaximo = anoAtual + 50;
            if (ano < 1900 || ano > anoMaximo) {
                throw new IllegalArgumentException("Ano inválido. Deve estar entre 1900 e " + anoMaximo);
            }
        }
        
        // Define mês e ano padrão como atual se não informado
        YearMonth mesAtual = YearMonth.now();
        int mesConsulta = (mes != null) ? mes : mesAtual.getMonthValue();
        int anoConsulta = (ano != null) ? ano : mesAtual.getYear();
        
        // Obtém a lista de equipamentos a serem analisados
        List<Equipamento> equipamentos = obterEquipamentos(equipamentoIds);
        
        // Calcula estatísticas para cada equipamento
        List<EstatisticasEquipamentoDTO> estatisticasEquipamentos = equipamentos.stream()
            .map(equipamento -> calcularEstatisticasEquipamento(equipamento, mesConsulta, anoConsulta))
            .collect(Collectors.toList());
        
        return new EstatisticasGeralEquipamentoDTO(estatisticasEquipamentos);
    }
    
    /**
     * Obtém a lista de equipamentos a serem analisados.
     * 
     * @param equipamentoIds lista de IDs (opcional)
     * @return lista de equipamentos
     */
    private List<Equipamento> obterEquipamentos(List<String> equipamentoIds) {
        if (equipamentoIds != null && !equipamentoIds.isEmpty()) {
            List<Equipamento> equipamentos = equipamentoRepository.findAllById(equipamentoIds);
            
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
            
            return equipamentos;
        }
        
        return equipamentoRepository.findAll();
    }
    
    /**
     * Calcula as estatísticas de um equipamento específico usando queries agregadas otimizadas.
     * 
     * @param equipamento equipamento a ser analisado
     * @param mes mês para consulta de reservas
     * @param ano ano para consulta de reservas
     * @return estatísticas do equipamento
     */
    private EstatisticasEquipamentoDTO calcularEstatisticasEquipamento(Equipamento equipamento, int mes, int ano) {
        // Calcula reservas do mês especificado usando query agregada
        ReservasMesDTO reservasDoMes = calcularReservasMesOtimizado(equipamento.getId(), mes, ano);
        
        // Calcula mês com mais reservas usando query agregada
        ReservasMesDTO mesComMaisReservas = calcularMesComMaisReservasOtimizado(equipamento.getId());
        
        // Calcula usuários que mais reservaram usando query agregada
        List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram = calcularUsuariosQueMaisReservaramOtimizado(equipamento.getId());
        
        return new EstatisticasEquipamentoDTO(
            equipamento.getId(),
            equipamento.getTombamento(),
            equipamento.getTipoEquipamento().getNome(),
            reservasDoMes,
            mesComMaisReservas,
            usuariosQueMaisReservaram
        );
    }
    
    /**
     * Calcula a quantidade de reservas em um mês específico usando query agregada.
     * 
     * @param equipamentoId ID do equipamento
     * @param mes mês
     * @param ano ano
     * @return estatísticas do mês
     */
    private ReservasMesDTO calcularReservasMesOtimizado(String equipamentoId, int mes, int ano) {
        Optional<ReservasPorMesProjection> resultado = solicitacaoReservaRepository
            .contarReservasPorEquipamentoEMes(equipamentoId, mes, ano);
        
        if (resultado.isPresent()) {
            ReservasPorMesProjection projection = resultado.get();
            return new ReservasMesDTO(
                projection.getMes(),
                projection.getAno(),
                projection.getTotalReservas(),
                projection.getReservasConfirmadas()
            );
        }
        
        // Se não houver reservas no mês, retorna zeros
        return new ReservasMesDTO(mes, ano, 0L, 0L);
    }
    
    /**
     * Calcula o mês com mais reservas usando query agregada.
     * 
     * @param equipamentoId ID do equipamento
     * @return estatísticas do mês com mais reservas
     */
    private ReservasMesDTO calcularMesComMaisReservasOtimizado(String equipamentoId) {
        List<ReservasPorMesProjection> reservasPorMes = solicitacaoReservaRepository
            .contarReservasPorEquipamentoAgrupadoPorMes(equipamentoId);
        
        if (reservasPorMes.isEmpty()) {
            // Retorna mês atual com zeros se não houver reservas
            YearMonth mesAtual = YearMonth.now();
            return new ReservasMesDTO(mesAtual.getMonthValue(), mesAtual.getYear(), 0L, 0L);
        }
        
        // A query já retorna ordenada por quantidade decrescente, pegamos o primeiro
        ReservasPorMesProjection mesComMais = reservasPorMes.get(0);
        
        return new ReservasMesDTO(
            mesComMais.getMes(),
            mesComMais.getAno(),
            mesComMais.getTotalReservas(),
            mesComMais.getReservasConfirmadas()
        );
    }
    
    /**
     * Calcula os usuários que mais reservaram o equipamento usando query agregada.
     * 
     * @param equipamentoId ID do equipamento
     * @return lista de usuários ordenada por quantidade de reservas (decrescente)
     */
    private List<UsuarioEstatisticaDTO> calcularUsuariosQueMaisReservaramOtimizado(String equipamentoId) {
        List<ReservasPorUsuarioProjection> reservasPorUsuario = solicitacaoReservaRepository
            .contarReservasPorEquipamentoAgrupadoPorUsuario(equipamentoId);
        
        // A query já retorna ordenada por quantidade decrescente
        return reservasPorUsuario.stream()
            .map(projection -> new UsuarioEstatisticaDTO(
                projection.getUsuarioId(),
                projection.getUsuarioNome(),
                projection.getTotalReservas(),
                projection.getReservasConfirmadas()
            ))
            .collect(Collectors.toList());
    }
}
