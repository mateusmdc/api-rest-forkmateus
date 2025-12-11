package br.uece.alunos.sisreserva.v1.dto.espaco;

/**
 * DTO para estatísticas de reservas em um determinado mês.
 * 
 * <p>Contém informações sobre a quantidade de reservas em um mês específico,
 * separadas entre solicitadas e confirmadas.</p>
 * 
 * @param mes mês (1-12)
 * @param ano ano
 * @param reservasSolicitadas quantidade de reservas solicitadas (status != APROVADO)
 * @param reservasConfirmadas quantidade de reservas confirmadas (status = APROVADO)
 */
public record ReservasMesDTO(
    Integer mes,
    Integer ano,
    Long reservasSolicitadas,
    Long reservasConfirmadas
) {}
