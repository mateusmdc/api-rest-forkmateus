package br.uece.alunos.sisreserva.v1.dto.espaco;

/**
 * DTO para estatísticas de uso por usuário.
 * 
 * <p>Contém informações sobre a quantidade de reservas que um usuário realizou,
 * separadas entre solicitadas e confirmadas.</p>
 * 
 * @param usuarioId identificador do usuário
 * @param usuarioNome nome do usuário
 * @param reservasSolicitadas quantidade de reservas solicitadas (qualquer status)
 * @param reservasConfirmadas quantidade de reservas confirmadas (status = APROVADO)
 */
public record UsuarioEstatisticaDTO(
    String usuarioId,
    String usuarioNome,
    Long reservasSolicitadas,
    Long reservasConfirmadas
) {}
