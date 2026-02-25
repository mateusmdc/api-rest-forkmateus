package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasMesDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.TotaisPeriodoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.UsuarioEstatisticaDTO;

import java.util.List;

/**
 * DTO para estatísticas de uso de um equipamento específico.
 * 
 * <p>Contém todas as informações estatísticas relacionadas ao uso de um equipamento,
 * incluindo estatísticas por mês do período, mês com mais reservas, usuários
 * que mais reservaram e totais do período.</p>
 * 
 * @param equipamentoId identificador do equipamento
 * @param equipamentoTombamento tombamento do equipamento
 * @param equipamentoTipo tipo do equipamento
 * @param equipamentoDescricao descrição do equipamento
 * @param estatisticasPorMes lista de estatísticas de cada mês do período consultado
 * @param mesComMaisReservas estatísticas do mês com mais solicitações confirmadas no período (null se período for de 1 mês)
 * @param usuariosQueMaisReservaram lista de usuários que mais tiveram reservas aprovadas no período, ordenada por quantidade (top 10)
 * @param todosUsuarios lista completa de todos os usuários que solicitaram reservas no período, ordenada por quantidade total
 * @param totaisPeriodo totais agregados de todo o período
 */
public record EstatisticasEquipamentoDTO(
    String equipamentoId,
    String equipamentoTombamento,
    String equipamentoTipo,
    String equipamentoDescricao,
    List<ReservasMesDTO> estatisticasPorMes,
    ReservasMesDTO mesComMaisReservas,
    List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram,
    List<UsuarioEstatisticaDTO> todosUsuarios,
    TotaisPeriodoDTO totaisPeriodo
) {}
