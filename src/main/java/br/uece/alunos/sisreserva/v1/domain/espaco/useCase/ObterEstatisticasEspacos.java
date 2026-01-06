package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasGeralDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasMesDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasPorMesProjection;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasPorUsuarioProjection;
import br.uece.alunos.sisreserva.v1.dto.espaco.UsuarioEstatisticaDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Caso de uso para obter estatísticas de uso dos espaços.
 * 
 * <p>Calcula e retorna estatísticas detalhadas sobre o uso dos espaços,
 * incluindo reservas do mês, mês com mais reservas e usuários que mais
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
     * Obtém estatísticas de uso dos espaços.
     * 
     * @param mes mês para filtrar reservas (opcional, padrão = mês atual)
     * @param ano ano para filtrar reservas (opcional, padrão = ano atual)
     * @param espacoIds lista de IDs de espaços para filtrar (opcional, padrão = todos os espaços)
     * @return estatísticas agrupadas por espaço
     */
    public EstatisticasGeralDTO obterEstatisticas(Integer mes, Integer ano, List<String> espacoIds) {
        // Define mês e ano padrão como atual se não informado
        YearMonth mesAtual = YearMonth.now();
        int mesConsulta = (mes != null) ? mes : mesAtual.getMonthValue();
        int anoConsulta = (ano != null) ? ano : mesAtual.getYear();
        
        // Obtém a lista de espaços a serem analisados
        List<Espaco> espacos = obterEspacos(espacoIds);
        
        // Calcula estatísticas para cada espaço
        List<EstatisticasEspacoDTO> estatisticasEspacos = espacos.stream()
            .map(espaco -> calcularEstatisticasEspaco(espaco, mesConsulta, anoConsulta))
            .collect(Collectors.toList());
        
        return new EstatisticasGeralDTO(estatisticasEspacos);
    }
    
    /**
     * Obtém a lista de espaços a serem analisados.
     * 
     * @param espacoIds lista de IDs (opcional)
     * @return lista de espaços
     */
    private List<Espaco> obterEspacos(List<String> espacoIds) {
        if (espacoIds != null && !espacoIds.isEmpty()) {
            List<Espaco> espacos = espacoRepository.findAllById(espacoIds);
            
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
            
            return espacos;
        }
        
        return espacoRepository.findAll();
    }
    
    /**
     * Calcula as estatísticas de um espaço específico usando queries agregadas otimizadas.
     * 
     * @param espaco espaço a ser analisado
     * @param mes mês para consulta de reservas
     * @param ano ano para consulta de reservas
     * @return estatísticas do espaço
     */
    private EstatisticasEspacoDTO calcularEstatisticasEspaco(Espaco espaco, int mes, int ano) {
        // Calcula reservas do mês especificado usando query agregada
        ReservasMesDTO reservasDoMes = calcularReservasMesOtimizado(espaco.getId(), mes, ano);
        
        // Calcula mês com mais reservas usando query agregada
        ReservasMesDTO mesComMaisReservas = calcularMesComMaisReservasOtimizado(espaco.getId());
        
        // Calcula usuários que mais reservaram usando query agregada
        List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram = calcularUsuariosQueMaisReservaramOtimizado(espaco.getId());
        
        return new EstatisticasEspacoDTO(
            espaco.getId(),
            espaco.getNome(),
            reservasDoMes,
            mesComMaisReservas,
            usuariosQueMaisReservaram
        );
    }
    
    /**
     * Calcula a quantidade de reservas em um mês específico usando query agregada.
     * 
     * @param espacoId ID do espaço
     * @param mes mês
     * @param ano ano
     * @return estatísticas do mês
     */
    private ReservasMesDTO calcularReservasMesOtimizado(String espacoId, int mes, int ano) {
        Optional<ReservasPorMesProjection> resultado = solicitacaoReservaRepository
            .contarReservasPorEspacoEMes(espacoId, mes, ano);
        
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
     * @param espacoId ID do espaço
     * @return estatísticas do mês com mais reservas
     */
    private ReservasMesDTO calcularMesComMaisReservasOtimizado(String espacoId) {
        List<ReservasPorMesProjection> reservasPorMes = solicitacaoReservaRepository
            .contarReservasPorEspacoAgrupadoPorMes(espacoId);
        
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
     * Calcula os usuários que mais reservaram o espaço usando query agregada.
     * 
     * @param espacoId ID do espaço
     * @return lista de usuários ordenada por quantidade de reservas (decrescente)
     */
    private List<UsuarioEstatisticaDTO> calcularUsuariosQueMaisReservaramOtimizado(String espacoId) {
        List<ReservasPorUsuarioProjection> reservasPorUsuario = solicitacaoReservaRepository
            .contarReservasPorEspacoAgrupadoPorUsuario(espacoId);
        
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
