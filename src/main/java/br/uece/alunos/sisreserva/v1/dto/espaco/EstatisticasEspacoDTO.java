package br.uece.alunos.sisreserva.v1.dto.espaco;

import java.util.List;

/**
 * DTO para estatísticas de uso de um espaço específico.
 * 
 * <p>Contém todas as informações estatísticas relacionadas ao uso de um espaço,
 * incluindo estatísticas por mês do período, mês com mais reservas, usuários
 * que mais reservaram e totais do período.</p>
 * 
 * @param espacoId identificador do espaço
 * @param espacoNome nome do espaço
 * @param estatisticasPorMes lista de estatísticas de cada mês do período consultado
 * @param mesComMaisReservas estatísticas do mês com mais solicitações confirmadas no período (null se período for de 1 mês)
 * @param usuariosQueMaisReservaram lista de usuários que mais tiveram reservas aprovadas no período, ordenada por quantidade (top 10)
 * @param todosUsuarios lista completa de todos os usuários que solicitaram reservas no período, ordenada por quantidade total
 * @param totaisPeriodo totais agregados de todo o período
 */
public record EstatisticasEspacoDTO(
    String espacoId,
    String espacoNome,
    List<ReservasMesDTO> estatisticasPorMes,
    ReservasMesDTO mesComMaisReservas,
    List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram,
    List<UsuarioEstatisticaDTO> todosUsuarios,
    TotaisPeriodoDTO totaisPeriodo
) {}
