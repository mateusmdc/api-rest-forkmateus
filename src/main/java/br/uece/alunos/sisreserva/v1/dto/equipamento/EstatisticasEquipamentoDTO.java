package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.dto.espaco.ReservasMesDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.UsuarioEstatisticaDTO;

import java.util.List;

/**
 * DTO para estatísticas de uso de um equipamento específico.
 * 
 * <p>Contém todas as informações estatísticas relacionadas ao uso de um equipamento,
 * incluindo reservas do mês atual/filtrado, mês com mais reservas e usuários
 * que mais reservaram.</p>
 * 
 * @param equipamentoId identificador do equipamento
 * @param equipamentoTombamento tombamento do equipamento
 * @param equipamentoTipo tipo do equipamento
 * @param reservasDoMes estatísticas de reservas do mês atual ou filtrado
 * @param mesComMaisReservas estatísticas do mês com mais reservas
 * @param usuariosQueMaisReservaram lista de usuários que mais reservaram, ordenada por quantidade total de reservas
 */
public record EstatisticasEquipamentoDTO(
    String equipamentoId,
    String equipamentoTombamento,
    String equipamentoTipo,
    ReservasMesDTO reservasDoMes,
    ReservasMesDTO mesComMaisReservas,
    List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram
) {}
