package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReserva;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.SolicitacaoReservaRepository;
import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EstatisticasGeralDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasMesDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.UsuarioEstatisticaDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Caso de uso para obter estatísticas de uso dos espaços.
 * 
 * <p>Calcula e retorna estatísticas detalhadas sobre o uso dos espaços,
 * incluindo reservas do mês, mês com mais reservas e usuários que mais
 * reservaram.</p>
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
     * Calcula as estatísticas de um espaço específico.
     * 
     * @param espaco espaço a ser analisado
     * @param mes mês para consulta de reservas
     * @param ano ano para consulta de reservas
     * @return estatísticas do espaço
     */
    private EstatisticasEspacoDTO calcularEstatisticasEspaco(Espaco espaco, int mes, int ano) {
        // Obtém todas as reservas do espaço
        List<SolicitacaoReserva> todasReservas = solicitacaoReservaRepository.findByEspacoId(espaco.getId());
        
        // Calcula reservas do mês especificado
        ReservasMesDTO reservasDoMes = calcularReservasMes(todasReservas, mes, ano);
        
        // Calcula mês com mais reservas
        ReservasMesDTO mesComMaisReservas = calcularMesComMaisReservas(todasReservas);
        
        // Calcula usuários que mais reservaram
        List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram = calcularUsuariosQueMaisReservaram(todasReservas);
        
        return new EstatisticasEspacoDTO(
            espaco.getId(),
            espaco.getNome(),
            reservasDoMes,
            mesComMaisReservas,
            usuariosQueMaisReservaram
        );
    }
    
    /**
     * Calcula a quantidade de reservas em um mês específico.
     * 
     * @param reservas lista de todas as reservas
     * @param mes mês
     * @param ano ano
     * @return estatísticas do mês
     */
    private ReservasMesDTO calcularReservasMes(List<SolicitacaoReserva> reservas, int mes, int ano) {
        // Filtra reservas do mês
        List<SolicitacaoReserva> reservasDoMes = reservas.stream()
            .filter(r -> {
                LocalDateTime dataInicio = r.getDataInicio();
                return dataInicio.getMonthValue() == mes && dataInicio.getYear() == ano;
            })
            .collect(Collectors.toList());
        
        // Conta todas as solicitadas e confirmadas (status = APROVADO)
        long solicitadas = reservasDoMes.size();
        
        long confirmadas = reservasDoMes.stream()
            .filter(r -> r.getStatus() == StatusSolicitacao.APROVADO)
            .count();
        
        return new ReservasMesDTO(mes, ano, solicitadas, confirmadas);
    }
    
    /**
     * Calcula o mês com mais reservas.
     * 
     * @param reservas lista de todas as reservas
     * @return estatísticas do mês com mais reservas
     */
    private ReservasMesDTO calcularMesComMaisReservas(List<SolicitacaoReserva> reservas) {
        if (reservas.isEmpty()) {
            // Retorna mês atual com zeros se não houver reservas
            YearMonth mesAtual = YearMonth.now();
            return new ReservasMesDTO(mesAtual.getMonthValue(), mesAtual.getYear(), 0L, 0L);
        }
        
        // Agrupa reservas por mês/ano
        Map<YearMonth, List<SolicitacaoReserva>> reservasPorMes = reservas.stream()
            .collect(Collectors.groupingBy(r -> 
                YearMonth.from(r.getDataInicio())
            ));
        
        // Encontra o mês com mais reservas totais
        YearMonth mesComMais = reservasPorMes.entrySet().stream()
            .max(Comparator.comparingInt(entry -> entry.getValue().size()))
            .map(Map.Entry::getKey)
            .orElse(YearMonth.now());
        
        List<SolicitacaoReserva> reservasDoMes = reservasPorMes.get(mesComMais);
        
        // Conta todas as solicitadas e confirmadas (status = APROVADO)
        long solicitadas = reservasDoMes.size();
        
        long confirmadas = reservasDoMes.stream()
            .filter(r -> r.getStatus() == StatusSolicitacao.APROVADO)
            .count();
        
        return new ReservasMesDTO(mesComMais.getMonthValue(), mesComMais.getYear(), solicitadas, confirmadas);
    }
    
    /**
     * Calcula os usuários que mais reservaram o espaço.
     * 
     * @param reservas lista de todas as reservas
     * @return lista de usuários ordenada por quantidade de reservas (decrescente)
     */
    private List<UsuarioEstatisticaDTO> calcularUsuariosQueMaisReservaram(List<SolicitacaoReserva> reservas) {
        // Agrupa reservas por usuário
        Map<Usuario, List<SolicitacaoReserva>> reservasPorUsuario = reservas.stream()
            .collect(Collectors.groupingBy(SolicitacaoReserva::getUsuarioSolicitante));
        
        // Calcula estatísticas por usuário
        List<UsuarioEstatisticaDTO> usuarios = reservasPorUsuario.entrySet().stream()
            .map(entry -> {
                Usuario usuario = entry.getKey();
                List<SolicitacaoReserva> reservasUsuario = entry.getValue();
                
                // Conta todas as solicitadas e confirmadas (status = APROVADO)
                long solicitadas = reservasUsuario.size();
                
                long confirmadas = reservasUsuario.stream()
                    .filter(r -> r.getStatus() == StatusSolicitacao.APROVADO)
                    .count();
                
                return new UsuarioEstatisticaDTO(
                    usuario.getId(),
                    usuario.getNome(),
                    solicitadas,
                    confirmadas
                );
            })
            .sorted((u1, u2) -> {
                // Ordena por total de reservas (solicitadas + confirmadas) em ordem decrescente
                long total1 = u1.reservasSolicitadas() + u1.reservasConfirmadas();
                long total2 = u2.reservasSolicitadas() + u2.reservasConfirmadas();
                return Long.compare(total2, total1);
            })
            .collect(Collectors.toList());
        
        return usuarios;
    }
}
