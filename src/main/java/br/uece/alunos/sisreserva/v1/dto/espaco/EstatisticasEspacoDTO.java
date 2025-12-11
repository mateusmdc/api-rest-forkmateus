package br.uece.alunos.sisreserva.v1.dto.espaco;

import java.util.List;

/**
 * DTO para estatísticas de uso de um espaço específico.
 * 
 * <p>Contém todas as informações estatísticas relacionadas ao uso de um espaço,
 * incluindo reservas do mês atual/filtrado, mês com mais reservas e usuários
 * que mais reservaram.</p>
 * 
 * @param espacoId identificador do espaço
 * @param espacoNome nome do espaço
 * @param reservasDoMes estatísticas de reservas do mês atual ou filtrado
 * @param mesComMaisReservas estatísticas do mês com mais reservas
 * @param usuariosQueMaisReservaram lista de usuários que mais reservaram, ordenada por quantidade total de reservas
 */
public record EstatisticasEspacoDTO(
    String espacoId,
    String espacoNome,
    ReservasMesDTO reservasDoMes,
    ReservasMesDTO mesComMaisReservas,
    List<UsuarioEstatisticaDTO> usuariosQueMaisReservaram
) {}
